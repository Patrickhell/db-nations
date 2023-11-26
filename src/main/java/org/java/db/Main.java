package org.java.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	
	private static final String url = "jdbc:mysql://localhost:8889/db_nation";
	private static final String user = "root";
	private static final String pws = "root";
	
	public static void main(String[] args) {
		
		 millestone1();
		millestone2Bonus();
		
		
	}
	
	
	private static void millestone1() {
		

		try( Connection con = DriverManager.getConnection(url, user, pws)) {
			
			final String sql = "SELECT countries.country_id, countries.name AS country_name, regions.name AS region_name, continents.name AS continent_name "
				    + "FROM countries "
				    + "JOIN regions ON countries.region_id = regions.region_id "
				    + "JOIN continents ON regions.continent_id = continents.continent_id "
				    + "ORDER BY countries.name;";

			
			PreparedStatement ps = con.prepareStatement(sql);
				
			    ResultSet rs = ps.executeQuery();
					
					while(rs.next()) {
						
						int id = rs.getInt(1);
						String country_name = rs.getString(2);
						String region_name = rs.getString(3);
						String continent_name = rs.getNString(4);
						
						System.out.println( "[" + id + "] " + country_name + " " + region_name + " " + continent_name );

					}
					
					System.out.println("\n-----------------------------------------\n");
					
			} catch (Exception e) {
			
			System.out.println("Error in db: " + e.getMessage());
		}
		
		
	}
	
	private static void millestone2Bonus() {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Search: ");
		
		String parola = sc.nextLine().toLowerCase();
		
		try (Connection con = DriverManager.getConnection(url, user, pws)) {
			
			final String SQL = ""
					+ "SELECT countries.country_id AS country_id, countries.name AS country_name, regions.name AS region_name, continents.name AS continent_name "
				    + "FROM countries "
				    + "JOIN regions ON countries.region_id = regions.region_id "
				    + "JOIN continents ON regions.continent_id = continents.continent_id "
				    + "WHERE countries.name LIKE   ?  " 
				    + "ORDER BY countries.name;" ;
			
			final String statsByCountryId = 
					   " SELECT * "
					 + " FROM country_stats "
					 + " WHERE country_stats.country_id = ? "
					 + " ORDER BY `year` DESC "
					 + " LIMIT 1; ";
			
			final String languagesByCountry = 
					   " SELECT * "
					 + " FROM languages"
					 + " JOIN country_languages "
					 + " ON languages.language_id = country_languages.language_id "
					 + " WHERE country_languages.country_id = ? ";
			
			PreparedStatement ps = con.prepareStatement(SQL);
				
			ps.setString(1, "%" + parola + "%");
			ResultSet rs = ps.executeQuery();
				
				  System.out.print( " ID: " + "\t   " +  "COUNTRY"  + "\t   " + "REGION: "  + "\t   " +  "CONTINENTE: "  +  "\n");
	
				while(rs.next()) {
						final int country_id = rs.getInt("country_id");
						final String country_name  = rs.getString("country_name");
						final String region_name = rs.getString("region_name");
						final String continent_name = rs.getString("continent_name");
						
						
				System.out.print( "[" + country_id + "]" + "\t   "  + country_name  + "\t   "  + region_name + "\t   "  + continent_name +  "\n");

				}
				
				System.out.println("\n---------------------------------------------------------\n");
				
				System.out.print("Choose a country id: ");
				short id = Short.valueOf(sc.nextLine());
				
				ps = con.prepareStatement(languagesByCountry);
				ps.setInt(1, id);
				rs = ps.executeQuery();
				
				String languages = "";
				while(rs.next()) {
					
					String language = rs.getString("language");
					languages += language + ", ";
				}
				
				ps = con.prepareStatement(statsByCountryId);
				ps.setInt(1, id);
				rs = ps.executeQuery();
				rs.next();
				
				System.out.println("Details:");
				System.out.println("Languages: " + languages);
				System.out.println("Most recent stats:");
				System.out.println("Year: " + rs.getString("year"));
				System.out.println("Population: " + rs.getString("population"));
				System.out.println("GDP: " + rs.getString("gdp"));
				
		}catch (Exception e) {
			
			System.out.println("Errore di connessione: " + e.getMessage());
		}
			   
	
	}

}
