
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Main
 */
@WebServlet("/index")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Main() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		response.setContentType("text/html");
		PrintWriter cast = response.getWriter();
		String check_uid = request.getParameter("uid");
		int uid = -1;
		try {
			Integer.valueOf(check_uid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (check_uid != "") {
			uid = Integer.parseInt(request.getParameter("uid").trim());
		}
		String url = "jdbc:mysql://localhost:3306/ATM?user=root&password=Letsroll";

		String r_action = null;
		// 1
		System.out.println(uid);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url);
			Statement st = con.createStatement();

			ResultSet r_set = st.executeQuery("Select * from accountInfo where userId = '" + uid + "'");

			if (r_set.next()) {

				r_action = "sucessful";

			} else {

				r_action = "unsucessful";

			}

			switch (r_action) {

			case "sucessful":
				r_action = "sucessful";

				cast.println("<html><head><title>LOGIN HERE</title></head><body></style>");
				cast.println("<center><H5 style =\"color:green \">You Are Successfully Logged In</H5></center>");
				cast.println("<form action=\"http://localhost:8080/project4/index\" method =\"post\"></center>");
				cast.println("<center><input type=\"hidden\" name=\"uid\" value=\" " + uid + "\" </input>");
				cast.println("<center>Enter Your AMOUNT: <input type=\"text\" name = \"amount\"</center><br>");
				cast.println("<center><input type=\"submit\" value =\"deposit\" name=\"submit\"></center>");
				cast.println("<center><input type=\"submit\" value =\"withdraw\" name=\"submit\"></center>");
				cast.println("</form></body></html>");

				String sql1 = "select * from accountInfo where userID = '" + uid + "'";
				ResultSet r_set2 = st.executeQuery(sql1);
				if (r_set2.next()) {
					float bal = r_set2.getFloat(3);
					String holder = r_set2.getString(2);
					String typ = request.getParameter("submit");
					String amount = request.getParameter("amount");
					float entered = 0;
					if (amount != null || amount != "") {
						entered = Float.parseFloat(amount.trim());
						if (entered <= 0) {
							cast.println("<center><H5 style =\"color:red\">Incorrect Amount</H5></center>");
							break;
						}
						if (typ.equals("deposit")) {

							String sql2 = "UPDATE accountInfo SET BALANCE = BALANCE + " + entered + " WHERE userID = "
									+ uid + "";
							final int rs = st.executeUpdate(sql2);
							String que = "select * from accountInfo where userID = '" + uid + "'";
							ResultSet rs5 = st.executeQuery(que);
							while (rs5.next()) {
								float check2 = rs5.getFloat(3);
								System.out.println(check2);
								cast.println("<center><p style =\"color:green\">" + holder
										+ " DEPOSITED AMOUNT SUCESSFULLY! </p></center>");
								cast.println(
										"<center><H5 style =\"color:green \">Balance : " + check2 + "$</H5 ></center>");
							}
						}

						else {
							if (bal < entered) {

								cast.println("<center><H5 style =\"color:red \">" + holder
										+ " Sorry,you do not have sufficient balance!</H5></center>");

							} else {

								String sql4 = "UPDATE ACCOUNTINFO SET BALANCE = BALANCE - " + entered
										+ " WHERE userID = " + uid + "";
								final int rs4 = st.executeUpdate(sql4);

								String sql3 = "select * from accountInfo where userID = '" + uid + "'";
								ResultSet rs3 = st.executeQuery(sql3);
								while (rs3.next()) {
									float check1 = rs3.getFloat(3);
									System.out.println(check1);
									cast.println("<center><H5 style =\"color:green \">" + holder
											+ " AMOUNT DISPENSED SUCCESSFULLY! </H5></center>");
									cast.println("<center><H5 style =\"color:red \">Balance : " + check1
											+ "$</H5></center>");
								}

								

							}

						}
					}
				}

				return;

			case "unsucessful":
				r_action = "unsucessful";
				cast.println("<html><head><title>LOGIN HERE</title></head><body>");
				cast.println(
						"<center><H5 style =\"color:red \" >YOUR AUTHENTICATION FAILED PLEASE TRY AGAIN!</H5></center>");
				cast.println("<form action=\"http://localhost:8080/project4/index\" method =\"post\">");
				cast.println("<center>USERID: <input type=\"text\" name = \"uid\"</center><br>");
				cast.println("<center><input type=\"submit\" value=\"Login\"></center>");
				cast.println("</form></body></html>");

				return;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
