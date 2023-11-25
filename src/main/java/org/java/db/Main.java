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
		millestone2();
		bonus();
		
		
	}
	
	
	private static void millestone1() {
		

		try( Connection con = DriverManager.getConnection(url, user, pws)) {
			
			final String sql = "SELECT countries.country_id, countries.name AS country_name, regions.name AS region_name, continents.name AS continent_name "
				    + "FROM countries "
				    + "JOIN regions ON countries.region_id = regions.region_id "
				    + "JOIN continents ON regions.continent_id = continents.continent_id "
				    + "ORDER BY countries.name;";

			
			try(PreparedStatement ps = con.prepareStatement(sql)) {
				
				try(ResultSet rs = ps.executeQuery()) {
					
					while(rs.next()) {
						
						int id = rs.getInt(1);
						String country_name = rs.getString(2);
						String region_name = rs.getString(3);
						String continent_name = rs.getNString(4);
						
						System.out.println( "[" + id + "] " + country_name + " " + region_name + " " + continent_name );

					}
					
					System.out.println("\n-----------------------------------------\n");
				}
			}
			
		}catch (Exception e) {
			
			System.out.println("Error in db: " + e.getMessage());
		}
		
		
	}
	
	private static void millestone2() {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("You Search: ");
		
		String parola = sc.nextLine().toLowerCase();
		
		try (Connection con = DriverManager.getConnection(url, user, pws)) {
			
			final String SQL = ""
					+ "SELECT countries.country_id AS country_id, countries.name AS country_name, regions.name AS region_name, continents.name AS continent_name "
				    + "FROM countries "
				    + "JOIN regions ON countries.region_id = regions.region_id "
				    + "JOIN continents ON regions.continent_id = continents.continent_id "
				    + "WHERE countries.name LIKE   ?  OR regions.name LIKE  ?  OR continents.name LIKE ? " 
				    + "ORDER BY countries.name;" ;
			
			try(PreparedStatement ps = con.prepareStatement(SQL)) {
				
				ps.setString(1, "%" + parola + "%");
				ps.setString(2, "%" + parola + "%");
				ps.setString(3, "%" + parola + "%");
				
				try(ResultSet rs = ps.executeQuery()) {
					
					while(rs.next()) {
						final int country_id = rs.getInt("country_id");
						final String country_name  = rs.getString("country_name");
						final String region_name = rs.getString("region_name");
						final String continent_name = rs.getString("continent_name");
						
			            System.out.print( " ID: " + country_id + " , " + "PAESE:" + country_name  + " , " + "REGIONE: "  + region_name + " , " + "CONTINENTE: " + continent_name + ";" + "\n");

					}
					System.out.println("\n----------------------------------------------------------\n");

				}
				
		    } catch (Exception e) {
		    	
		    	System.out.println("Error in db: " + e.getMessage());
		    }
		    	
		    } catch (Exception e1) {
				
			}
	}
	
	
	private static void bonus() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Scegli l'ID di un paese: ");
		
		
		String strId  = sc.nextLine();
		short id = Short.valueOf(strId);
		
		try(Connection con = DriverManager.getConnection(url, user, pws)) {
			
			final String SQL = ""
				    + "SELECT DISTINCT countries.country_id AS country_id, countries.name AS country_name, languages.language AS language_name, country_stats.year AS country_year, country_stats.population AS country_population, country_stats.gdp AS country_gdp "
				    + "FROM countries "
				    + "JOIN country_stats ON country_stats.country_id = countries.country_id "
				    + "JOIN languages ON languages.language_id = countries.country_id "
				    + "WHERE country_stats.year = 2018 AND countries.country_id = ?;";

			
			try(PreparedStatement ps = con.prepareStatement(SQL)) {
				
				ps.setShort(1, id);
				
				try(ResultSet rs = ps.executeQuery()) {
					
					
					while(rs.next()) {
						
						final String country_name = rs.getString(2);
						final String language_name = rs.getString(3);
						final String country_year = rs.getString(4);
						final String country_population = rs.getString(5);
						final String country_gdp = rs.getString(6);
						
						System.out.println("Details for Country: " + " " + country_name + "\n"
								                                   + "Languages: " + language_name + "\n"
								                                   + "Most recent stats" + "\n"
								                                   + "Year: " + country_year + "\n"
								                                   + "Population: " + country_population + "\n"
								                                   + "GDP: " + country_gdp
								                                   );						
					}
					
				}
				
				
			}catch (Exception e) {
				
				System.out.println("Error in db: " + e.getMessage());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
		}
	}

}
