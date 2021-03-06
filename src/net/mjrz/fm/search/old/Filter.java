/*******************************************************************************
 * Copyright  
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.mjrz.fm.search.old;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;

import net.mjrz.fm.Main;
import net.mjrz.fm.entity.beans.types.EDouble;
import net.mjrz.fm.entity.beans.types.EString;
import net.mjrz.fm.utils.crypto.CHelper;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author iFreeBudget ifreebudget@gmail.com
 * 
 */
public class Filter implements Cloneable {
	private String name;

	private ArrayList<Predicate> filter;
	private ArrayList<String> operatorList;

	public static final String AND_OPERATOR = "AND";
	public static final String OR_OPERATOR = "OR";

	private ArrayList<Order> order;

	private ArrayList<Group> groups = null;
	private ArrayList<String> groupOperators = null;

	private ArrayList<String> selectWhat;

	private String filterObject = null;

	private Logger log = Logger.getLogger(Filter.class);

	public Filter() {
		name = System.currentTimeMillis() + ".xml";
		filter = new ArrayList<Predicate>();
		operatorList = new ArrayList<String>();

		groups = new ArrayList<Group>();
		groupOperators = new ArrayList<String>();
	}

	public void breakGroup(String operator) {
		Group curr = new Group();
		groups.add(curr);
		for (Predicate f : filter) {
			curr.getFilter().add(f);
		}
		for (String s : operatorList) {
			curr.getOperators().add(s);
		}
		groupOperators.add(operator);
		filter.clear();
		operatorList.clear();
	}

	public String toString() {
		return name;
	}

	public String getFilterName() {
		return name;
	}

	public void setFilterName(String n) {
		name = n;
	}

	public void addOrder(Order o) {
		if (order == null)
			order = new ArrayList<Order>();
		// order.clear();
		order.add(o);
	}

	public void clearOrder() {
		if (order != null)
			order.clear();
	}

	public ArrayList<Order> getOrder() {
		return order;
	}

	public boolean addPredicate(Predicate p, String operator) {
		if (!p.isValid())
			return false;

		int pos = filter.indexOf(p);
		if (pos < 0) {
			filter.add(p);
			operatorList.add(operator);
		}
		else {
			filter.set(pos, p);
			operatorList.set(pos, operator);
		}
		return true;
	}

	public void setSelectWhat(ArrayList<String> what) {
		selectWhat = what;
	}

	public String getSelectString(boolean count) {
		if (count) {
			return " count(R) ";
		}
		StringBuilder builder = new StringBuilder(" R ");
		if (selectWhat != null) {
			for (int i = 0; i < selectWhat.size(); i++) {
				builder.append("." + selectWhat.get(i));
				if (i < selectWhat.size() - 1) {
					builder.append(",");
				}
			}
		}
		return builder.toString();
	}

	public boolean hasPredicates() {
		return filter.size() > 0;
	}

	public boolean hasPredicate(String key) {
		for (int i = 0; i < filter.size(); i++) {
			Predicate p = filter.get(i);
			if (p.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	public Predicate getPredicate(String key) {
		for (int i = 0; i < filter.size(); i++) {
			Predicate p = filter.get(i);
			if (p.getKey().equals(key)) {
				return p;
			}
		}
		return null;
	}

	public void clearPredicate(String s) {
		Predicate p = new Predicate(s);
		filter.remove(p);
	}

	public String getFilterObject() {
		if (filterObject == null)
			return "TT";
		else
			return filterObject;
	}

	public void setFilterObject(String obj) {
		filterObject = obj;
	}

	public String printFilter(boolean count) {
		/* If any ungrouped filters exist, add them to a group before processing */
		if (filter.size() > 0) {
			breakGroup(AND_OPERATOR);
		}

		StringBuffer query = new StringBuffer();
		query.append("select " + getSelectString(count) + " from "
				+ getFilterObject() + " R ");

		/* First add aroups */
		if (groups.size() > 0) {
			query.append(" where ");
			for (int i = 0; i < groups.size(); i++) {
				Group g = groups.get(i);
				query.append(printSubFilter(g.getFilter(), g.getOperators()));
				if (i < groups.size() - 1)
					query.append(" " + groupOperators.get(i) + " ");
			}
		}
		/* Add any remaining filters */
		if (filter.size() > 0) {
			query.append(printSubFilter(filter, operatorList));
		}
		if (!count) {
			if (order != null) {
				query.append(" order by ");
				int curr = 0;
				int sz = order.size();
				for (Order o : order) {
					query.append(o);
					if (curr < sz - 1) {
						query.append(",");
					}
					curr++;
				}
			}
		}
		log.debug("* " + query);
		return query.toString();
	}

	private String printSubFilter(ArrayList<Predicate> predicates,
			ArrayList<String> operators) {
		StringBuffer query = new StringBuffer("(");
		for (int i = 0; i < predicates.size(); i++) {
			Predicate p = predicates.get(i);
			query.append(p);
			if (i < predicates.size() - 1) {
				query.append(" " + operators.get(i) + " ");
			}
		}
		query.append(")");

		return query.toString();
	}

	public Query getQueryObject(Session s, boolean countQuery) throws Exception {
		try {
			int count = 0;
			Query q = null;
			if (countQuery) {
				q = s.createQuery(printFilter(true));
			}
			else {
				q = s.createQuery(printFilter(false));
			}
			for (Group g : groups) {
				ArrayList<Predicate> subFilter = g.getFilter();
				for (int i = 0; i < subFilter.size(); i++) {
					Predicate p = subFilter.get(i);
					if (!p.hasValues())
						continue;

					String type = p.getType();
					ArrayList<String> values = p.getValues();
					int sz = values.size();
					//
					for (int j = 0; j < sz; j++) {
						if (type.equals(Double.class.getName())) {
							Double d = Double.parseDouble(values.get(j));
							q.setDouble(count, d);
							count++;
						}
						else if (type.equals(Date.class.getName())) {
							Date d = Predicate.sdf.parse(values.get(j));
							q.setDate(count, d);
							count++;
						}
						else if (type.equals(EString.class.getName())) {
							q.setString(count, CHelper.encrypt(values.get(j)));
							count++;
						}
						else if (type.equals(EDouble.class.getName())) {
							q.setString(count, CHelper.encrypt(values.get(j)));
							count++;
						}
						else if (type.equals(Long.class.getName())) {
							q.setLong(count, Long.parseLong(values.get(j)));
							count++;
						}
						else if (type.equals(String.class.getName())) {
							if (p.getRel().equals("like")) {
								q.setString(count, "%" + values.get(j) + "%");
								count++;
							}
							else {
								q.setString(count, values.get(j));
								count++;
							}
						}
						else {
							q.setString(count, values.get(j));
							count++;
						}
					}
					//

				}
			}
			// System.out.println("Query = " + q.getQueryString());
			return q;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public boolean saveFilter(String homedir, String fname) throws Exception {
		BufferedWriter out = null;
		boolean ok = true;
		try {
			if (!validateFilter()) {
				ok = false;
				return ok;
			}
			File dir = new File(homedir);
			if (!dir.exists()) {
				ok = dir.mkdir();
			}
			if (ok) {
				File f = new File(dir.getAbsolutePath() + Main.PATH_SEPARATOR
						+ fname);
				if (f.exists())
					return false;
				name = fname;
				out = new BufferedWriter(new FileWriter(f));
				out.write(toXml());
			}
			return ok;
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (out != null)
				out.close();
		}
	}

	public String toXml() {
		StringBuffer query = new StringBuffer();
		query.append("<filter>");
		query.append("<name>" + name + "</name>");
		for (int i = 0; i < filter.size(); i++) {
			Predicate p = filter.get(i);
			query.append(p.toXml());
		}
		query.append("</filter>");
		return query.toString();
	}

	public boolean validateFilter() {
		boolean ret = true;
		if (filter.size() == 0)
			return false;
		for (int i = 0; i < filter.size(); i++) {
			Predicate p = filter.get(i);
			if (!p.isValid()) {
				clearPredicate(p.getKey());
				ret = false;
				break;
			}
		}
		return ret;
	}

	public final Object clone() throws CloneNotSupportedException {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			return null;
		}
	}

	private static class Group {
		ArrayList<Predicate> filter;
		ArrayList<String> operators;

		public Group() {
			filter = new ArrayList<Predicate>();
			operators = new ArrayList<String>();
		}

		public ArrayList<Predicate> getFilter() {
			return filter;
		}

		public ArrayList<String> getOperators() {
			return operators;
		}
	}
}
