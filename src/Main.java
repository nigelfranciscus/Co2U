import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import lombok.Data;

public class Main {

	public static void main(String[] args) {
		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");

		CsvParser parser = new CsvParser(settings);

		// parses all rows in one go.
		Map<CO2Key, CO2Value> co2List = new HashMap<CO2Key, CO2Value>();
		Map<CO2aKey, CO2aValue> co2aList = new HashMap<CO2aKey, CO2aValue>();
		Map<RegionKey, RegionValue> regionList = new HashMap<RegionKey, RegionValue>();

		try {
			List<String[]> allRows;
			List<String[]> allRowsa;
			List<String[]> regionRow;
			allRows = parser.parseAll(new FileReader("C:/Users/s2876731.STAFF/Desktop/NewCO2/airport_airport.csv"));
			allRowsa = parser.parseAll(new FileReader("C:/Users/s2876731.STAFF/Desktop/NewCO2/city_city.csv"));
			regionRow = parser.parseAll(new FileReader("C:/Users/s2876731.STAFF/Desktop/NewCO2/region.csv"));

			// airport csv

			for (String[] row : allRows) {

				// if the row is null then skip
				if (row[0] == null || row[0].length() == 0) {
					break;
				}

				// indexing
				// price1 = premium carbon
				// price2 = economy carbon
				CO2Key co2Key = new CO2Key();
				CO2Value CO2Value = new CO2Value();

				co2Key.setStartAirport(row[0]);
				co2Key.setEndAirport(row[1]);
				CO2Value.setDistance(Double.valueOf(row[2]));
				CO2Value.setPrice1(Double.valueOf(row[6]));
				CO2Value.setPrice2(Double.valueOf(row[7]));
				co2List.put(co2Key, CO2Value);
			}

			// city csv

			for (String[] row : allRowsa) {

				// if the row is null then skip
				if (row[0] == null || row[0].length() == 0) {
					break;
				}

				// indexing
				// price1 = premium carbon
				// price2 = economy carbon
				CO2aKey co2aKey = new CO2aKey();
				CO2aValue CO2aValue = new CO2aValue();

				co2aKey.setStartAirporta(row[0]);
				co2aKey.setEndAirporta(row[1]);
				CO2aValue.setDistancea(Double.valueOf(row[2]));
				CO2aValue.setPrice1a(Double.valueOf(row[6]));
				CO2aValue.setPrice2a(Double.valueOf(row[7]));
				co2aList.put(co2aKey, CO2aValue);
			}

			// region csv

			for (String[] row : regionRow) {

				// if the row is null then skip
				if (row[0] == null || row[0].length() == 0) {
					break;
				}

				RegionKey regionKey = new RegionKey();
				RegionValue regionValue = new RegionValue();

				regionKey.setAirport(row[0]);
				regionValue.setCity(row[1]);
				regionValue.setCountry(row[2]);
				regionValue.setRegion(row[3]);
				regionList.put(regionKey, regionValue);
			}

			BufferedReader lineReader = new BufferedReader(new FileReader(
					"C:/Users/s2876731.STAFF/Documents/AIRtravel/New Monthly/New 2017/griffith_2017m01.csv"));
			FileWriter writer = new FileWriter(
					"C:/Users/s2876731.STAFF/Documents/AIRtravel/New Monthly/New 2017/Results/griffith_2017m01.csv");
			FileWriter missingAirport = new FileWriter(
					"C:/Users/s2876731.STAFF/Documents/AIRtravel/New Monthly/New 2017/Results/missing.csv");

			writer.write("year" + "," + "month" + "," + "origin_airport" + "," + "CX1_AIRPORT" + "," + "CX2_AIRPORT"
					+ "," + "destination_airport" + "," + "airline_1" + "," + "airline_2" + "," + "airline_3" + ","
					+ "pax" + "," + "pax_in_first_class" + "," + "pax_in_business_class" + "," + "pax_in_economy_class"
					+ "," + "Distance1" + "," + "Distance2" + "," + "Distance3" + "," + "SUMDISTANCE" + ","
					+ "DIRECTDISTANCE" + "," + "CO2P1" + "," + "CO2E1" + "," + "CO2P2" + "," + "CO2E2" + "," + "CO2P3"
					+ "," + "CO2E3" + "," + "SUMCO2P" + "," + "SUMCO2E" + "," + "origin_city" + "," + "origin_country"
					+ "," + "origin_region" + "," + "destination_city" + "," + "destination_country" + ","
					+ "destination_region");
			writer.write("\n");

			missingAirport.write("origin_airport," + "destination_airport");
			missingAirport.write("\n");

			int totalLines = 0;
			int totalPercent = 0;
			String newLine;
			while ((newLine = lineReader.readLine()) != null) {
				++totalLines;
				
				String[] row = newLine.split(",");

				if (row[0] == null || row[0].length() == 0 || row[2] == null || row[2].length() == 0 || row[5] == null
						|| row[5].length() == 0) {
					continue;
				}

				CO2Out co2Out = new CO2Out();
				co2Out.setDEP(row[2] != null ? row[2] : "");
				co2Out.setCON1(row[3] != null ? row[3] : "");
				co2Out.setCON2(row[4] != null ? row[4] : "");
				co2Out.setARR(row[5] != null ? row[5] : "");
				co2Out.setFLIGHT1(row[6] != null ? row[6] : "");
				co2Out.setFLIGHT2(row[7] != null ? row[7] : "");
				co2Out.setFLIGHT3(row[8] != null ? row[8] : "");

				/*
				 * Handle when the third cell is empty, the fourth is definitely
				 * empty
				 */
				if (row[3] == null || row[3].length() == 0) {
					
					
					// Use airport_airport
					CO2Key co2Key = new CO2Key();
					co2Key.setStartAirport(row[2]);
					co2Key.setEndAirport(row[5]);

					// Use city_city
					CO2aKey co2aKey = new CO2aKey();
					co2aKey.setStartAirporta(row[2]);
					co2aKey.setEndAirporta(row[5]);

					CO2Value co2Value = co2List.get(co2Key);
					CO2aValue co2Valuea = co2aList.get(co2aKey);

					// for (CO2Key co2Key : co2List.keySet()) {
					// if (co2Value == null) {
					// String key = name.toString();
					// String value = co2List.get(name).toString();
					// System.out.println(key + " " + value);
					// //missingAirport.write(key + "\n");
					// }
					// }

					if (co2Value != null) {
						co2Out.getDISTs()[2] = co2Value.getDistance();
						co2Out.getCO2Es()[2] = co2Value.getPrice1();
						co2Out.getCO2Ps()[2] = co2Value.getPrice2();
					}
					if (co2Value == null) {
						co2Out.setMISSINGORIGIN(row[2] != null ? row[2] : "");
						co2Out.setMISSINGDESTINATION(row[5] != null ? row[5] : "");
					} else if (co2Valuea != null) {
						co2Out.getDISTs()[2] = co2Valuea.getDistancea();
						co2Out.getCO2Es()[2] = co2Valuea.getPrice1a();
						co2Out.getCO2Ps()[2] = co2Valuea.getPrice2a();
					} else {
						co2Out.getCO2Es()[2] = (Double.valueOf(row[9]) * 0.1);
						co2Out.getCO2Ps()[2] = (Double.valueOf(row[9]) * 0.1);
					}
					
					
				}
				//System.out.println(first);
				/*
				 * Handle when the third cell is not empty, but the fourth is
				 * empty
				 */
				
				if (row[3].length() > 0 && (row[4] == null || row[4].length() == 0)) {
					/*
					 * Set the values for the first cell of values
					 */
					
					
					CO2Key co2Key = new CO2Key();
					co2Key.setStartAirport(row[2]);
					co2Key.setEndAirport(row[3]);

					CO2aKey co2aKey = new CO2aKey();
					co2aKey.setStartAirporta(row[2]);
					co2aKey.setEndAirporta(row[3]);

					CO2Value co2Value = co2List.get(co2Key);
					CO2aValue co2Valuea = co2aList.get(co2aKey);

					if (co2Value != null) {
						co2Out.getDISTs()[0] = co2Value.getDistance();
						co2Out.getCO2Es()[0] = co2Value.getPrice1();
						co2Out.getCO2Ps()[0] = co2Value.getPrice2();
					}
					if (co2Value == null) {
						co2Out.setMISSINGORIGIN(row[2] != null ? row[2] : "");
						co2Out.setMISSINGDESTINATION(row[3] != null ? row[3] : "");
					} else if (co2Valuea != null) {
						co2Out.getDISTs()[0] = co2Valuea.getDistancea();
						co2Out.getCO2Es()[0] = co2Valuea.getPrice1a();
						co2Out.getCO2Ps()[0] = co2Valuea.getPrice2a();
					} else {
						co2Out.getCO2Es()[0] = (Double.valueOf(row[9]) * 0.1);
						co2Out.getCO2Ps()[0] = (Double.valueOf(row[9]) * 0.1);
						
					}
					

					/*
					 * Set the values for the third cell of values
					 */
					co2Key.setStartAirport(row[3]);
					co2Key.setEndAirport(row[5]);
					co2Value = co2List.get(co2Key);

					co2aKey.setStartAirporta(row[3]);
					co2aKey.setEndAirporta(row[5]);
					co2Valuea = co2aList.get(co2aKey);

					if (co2Value != null) {
						co2Out.getDISTs()[2] = co2Value.getDistance();
						co2Out.getCO2Es()[2] = co2Value.getPrice1();
						co2Out.getCO2Ps()[2] = co2Value.getPrice2();
					}
					if (co2Value == null) {
						co2Out.setMISSINGORIGIN(row[3] != null ? row[3] : "");
						co2Out.setMISSINGDESTINATION(row[5] != null ? row[5] : "");
					} else if (co2Valuea != null) {
						co2Out.getDISTs()[2] = co2Valuea.getDistancea();
						co2Out.getCO2Es()[2] = co2Valuea.getPrice1a();
						co2Out.getCO2Ps()[2] = co2Valuea.getPrice2a();
					} else {
						co2Out.getCO2Es()[2] = (Double.valueOf(row[9]) * 0.1);
						co2Out.getCO2Ps()[2] = (Double.valueOf(row[9]) * 0.1);
						
					}

				}
				//System.out.println(first1);

				/*
				 * Handle when all cells are not empty
				 */

				if (row[4].length() > 0) {
					/*
					 * Set the values for the first cell of values
					 */
					CO2Key co2Key = new CO2Key();
					co2Key.setStartAirport(row[2]);
					co2Key.setEndAirport(row[3]);

					CO2aKey co2aKey = new CO2aKey();
					co2aKey.setStartAirporta(row[2]);
					co2aKey.setEndAirporta(row[3]);

					CO2Value co2Value = co2List.get(co2Key);
					CO2aValue co2Valuea = co2aList.get(co2aKey);

					if (co2Value != null) {
						co2Out.getDISTs()[0] = co2Value.getDistance();
						co2Out.getCO2Es()[0] = co2Value.getPrice1();
						co2Out.getCO2Ps()[0] = co2Value.getPrice2();
					}
					if (co2Value == null) {
						co2Out.setMISSINGORIGIN(row[2] != null ? row[2] : "");
						co2Out.setMISSINGDESTINATION(row[3] != null ? row[3] : "");
					} else if (co2Valuea != null) {
						co2Out.getDISTs()[0] = co2Valuea.getDistancea();
						co2Out.getCO2Es()[0] = co2Valuea.getPrice1a();
						co2Out.getCO2Ps()[0] = co2Valuea.getPrice2a();
					} else {
						co2Out.getCO2Es()[0] = (Double.valueOf(row[9]) * 0.1);
						co2Out.getCO2Ps()[0] = (Double.valueOf(row[9]) * 0.1);
					}

					/*
					 * Set the values for the second cell of values
					 */
					
					co2Key.setStartAirport(row[3]);
					co2Key.setEndAirport(row[4]);
					co2Value = co2List.get(co2Key);

					co2aKey.setStartAirporta(row[3]);
					co2aKey.setEndAirporta(row[4]);
					co2Valuea = co2aList.get(co2aKey);

					if (co2Value != null) {
						co2Out.getDISTs()[1] = co2Value.getDistance();
						co2Out.getCO2Es()[1] = co2Value.getPrice1();
						co2Out.getCO2Ps()[1] = co2Value.getPrice2();
					}
					if (co2Value == null) {
						co2Out.setMISSINGORIGIN(row[3] != null ? row[3] : "");
						co2Out.setMISSINGDESTINATION(row[4] != null ? row[4] : "");
					} else if (co2Valuea != null) {
						co2Out.getDISTs()[1] = co2Valuea.getDistancea();
						co2Out.getCO2Es()[1] = co2Valuea.getPrice1a();
						co2Out.getCO2Ps()[1] = co2Valuea.getPrice2a();
					} else {
						co2Out.getCO2Es()[1] = (Double.valueOf(row[9]) * 0.1);
						co2Out.getCO2Ps()[1] = (Double.valueOf(row[9]) * 0.1);
					}

					/*
					 * Set the values for the third cell of values
					 */
					co2Key.setStartAirport(row[4]);
					co2Key.setEndAirport(row[5]);
					co2Value = co2List.get(co2Key);

					co2aKey.setStartAirporta(row[4]);
					co2aKey.setEndAirporta(row[5]);
					co2Valuea = co2aList.get(co2aKey);

					if (co2Value != null) {
						co2Out.getDISTs()[2] = co2Value.getDistance();
						co2Out.getCO2Es()[2] = co2Value.getPrice1();
						co2Out.getCO2Ps()[2] = co2Value.getPrice2();
					}
					if (co2Value == null) {
						co2Out.setMISSINGORIGIN(row[4] != null ? row[4] : "");
						co2Out.setMISSINGDESTINATION(row[5] != null ? row[5] : "");
					} else if (co2Valuea != null) {
						co2Out.getDISTs()[2] = co2Valuea.getDistancea();
						co2Out.getCO2Es()[2] = co2Valuea.getPrice1a();
						co2Out.getCO2Ps()[2] = co2Valuea.getPrice2a();
					} else {
						co2Out.getCO2Es()[2] = (Double.valueOf(row[9]) * 0.1);
						co2Out.getCO2Ps()[2] = (Double.valueOf(row[9]) * 0.1);
					}
				}

				co2Out.setSUM(co2Out.getDISTs()[0] + co2Out.getDISTs()[1] + co2Out.getDISTs()[2]);
				co2Out.setSUMCO2E(co2Out.getCO2Es()[0] + co2Out.getCO2Es()[1] + co2Out.getCO2Es()[2]);
				co2Out.setSUMCO2P(co2Out.getCO2Ps()[0] + co2Out.getCO2Ps()[1] + co2Out.getCO2Ps()[2]);

				// Direct Distance
				CO2Key co2Key = new CO2Key();
				CO2aKey co2aKey = new CO2aKey();

				co2Key.setStartAirport(row[2]);
				co2Key.setEndAirport(row[5]);

				co2aKey.setStartAirporta(row[2]);
				co2aKey.setEndAirporta(row[5]);

				CO2Value value = co2List.get(co2Key);
				CO2aValue valuea = co2aList.get(co2aKey);
				if (value != null) {
					co2Out.setDIRECT(value.getDistance());
				} else if (valuea != null) {
					co2Out.setDIRECT(valuea.getDistancea());
				}

				co2Out.setYEAR(row[0]);
				co2Out.setMONTH(row[1]);
				co2Out.setDIRECTDIS(Double.valueOf(row[9]));

				// PAX
				co2Out.setPAX_FIRST(Double.valueOf(row[10]));
				co2Out.setPAX_BUS(Double.valueOf(row[11]));
				co2Out.setPAX_ECO(Double.valueOf(row[12]));

				double number1 = 0, number2 = 0, number3 = 0;
				if (row[10] != null && row[10].trim().length() != 0) {
					number1 = Double.valueOf(row[10]);
				}
				if (row[11] != null && row[11].trim().length() != 0) {
					number2 = Double.valueOf(row[11]);
				}
				if (row[12] != null && row[12].trim().length() != 0) {
					number3 = Double.valueOf(row[12]);
				}

				co2Out.setPAX(number1 + number2 + number3);

				// City-Country-Region

				RegionKey airportDepKey = new RegionKey();
				airportDepKey.setAirport(row[2]);
				RegionValue airportDepValue = regionList.get(airportDepKey);

				if (airportDepValue != null) {
					// departure
					co2Out.setORIGIN_CITY(airportDepValue.getCity());
					co2Out.setORIGIN_COUNTRY(airportDepValue.getCountry());
					co2Out.setORIGIN_REGION(airportDepValue.getRegion());
				}

				RegionKey airportDesKey = new RegionKey();
				airportDesKey.setAirport(row[5]);
				RegionValue airportDesValue = regionList.get(airportDesKey);

				if (airportDesValue != null) {
					// destination
					co2Out.setDEST_CITY(airportDesValue.getCity());
					co2Out.setDEST_COUNTRY(airportDesValue.getCountry());
					co2Out.setDEST_REGION(airportDesValue.getRegion());
				}

				// Writing to result csv

				writer.append(co2Out.getYEAR() + ",");
				writer.append(co2Out.getMONTH() + ",");
				writer.append(co2Out.getDEP() + ",");
				writer.append(co2Out.getCON1() + ",");
				writer.append(co2Out.getCON2() + ",");
				writer.append(co2Out.getARR() + ",");
				writer.append(co2Out.getFLIGHT1() + ",");
				writer.append(co2Out.getFLIGHT2() + ",");
				writer.append(co2Out.getFLIGHT3() + ",");
				writer.append(co2Out.getPAX() + ",");
				writer.append(co2Out.getPAX_FIRST() + ",");
				writer.append(co2Out.getPAX_BUS() + ",");
				writer.append(co2Out.getPAX_ECO() + ",");
				for (int i = 0; i < 3; i++) {
					writer.append(co2Out.getDISTs()[i] + ",");
				}
				writer.append(co2Out.getSUM() + ",");
				writer.append(co2Out.getDIRECTDIS() + ",");

				for (int i = 0; i < 3; i++) {
					writer.append(co2Out.getCO2Ps()[i] + ",");
					writer.append(co2Out.getCO2Es()[i] + ",");
				}

				writer.append(co2Out.getSUMCO2P() + ",");
				writer.append(co2Out.getSUMCO2E() + ",");

				writer.append(co2Out.getORIGIN_CITY() + ",");
				writer.append(co2Out.getORIGIN_COUNTRY() + ",");
				writer.append(co2Out.getORIGIN_REGION() + ",");
				writer.append(co2Out.getDEST_CITY() + ",");
				writer.append(co2Out.getDEST_COUNTRY() + ",");
				writer.append(co2Out.getDEST_REGION() + ",");
				writer.append("\n");
				
				if (co2Out.getMISSINGORIGIN() != null && co2Out.getMISSINGORIGIN().length() != 0) {
					missingAirport.append(co2Out.getMISSINGORIGIN() + ",");
					totalPercent ++;
				}
				if (co2Out.getMISSINGDESTINATION() != null && co2Out.getMISSINGDESTINATION().length() != 0) {
					missingAirport.append(co2Out.getMISSINGDESTINATION());
				} else{
					continue;
				}
				missingAirport.append("\n");
				
				
				
			}

			/*
			 * allRows = parser.parseAll(new FileReader(
			 * "C:/Users/s2876731/Desktop/itineraries_griffith_2015m02.csv"));
			 * 
			 * for (int rowIndex = 1; rowIndex < allRows.size(); rowIndex++) {
			 * CO2Out co2Out = new CO2Out(); String[] row =
			 * allRows.get(rowIndex);
			 * 
			 * }
			 */
			
			System.out.println(totalPercent);
			System.out.println(totalLines);
			float directDistPercent = (float) totalPercent / (float) totalLines * 100;
			System.out.println(String.format("%.0f%%",directDistPercent));
			
			lineReader.close();
			writer.flush();
			writer.close();
			missingAirport.close();
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Lombok

	@Data
	private static class CO2Out {
		String YEAR;
		String MONTH;
		String DEP;
		String CON1;
		String CON2;
		String ARR;
		String FLIGHT1;
		String FLIGHT2;
		String FLIGHT3;

		double PAX;
		double PAX_FIRST;
		double PAX_BUS;
		double PAX_ECO;

		String ORIGIN_CITY;
		String ORIGIN_COUNTRY;
		String ORIGIN_REGION;

		String CX1_CITY;
		String CX1_COUNTRY;
		String CX1_REGION;
		double DEST1;

		String CX2_CITY;
		String CX2_COUNTRY;
		String CX2_REGION;
		double DEST2;

		String DEST_CITY;
		String DEST_COUNTRY;
		String DEST_REGION;
		double DEST3;

		String MAIN_AIR;

		double[] DISTs;
		double SUM;
		double ACTUALSUM;
		double[] CO2Ps;
		double[] CO2Es;

		double SUMCO2P;
		double SUMCO2E;
		double DIST;
		double FIRST;
		double BUS;
		double ECON;
		double DIRECT;
		double DIRECTDIS;

		CO2Out() {
			DEP = null;
			DISTs = new double[3];
			CO2Ps = new double[3];
			CO2Es = new double[3];
		}

		String MISSINGORIGIN;
		String MISSINGDESTINATION;

	}

	@Data
	private static class CO2Value {
		double distance;
		double price1;
		double price2;
	}

	@Data
	private static class CO2Key {
		String startAirport;
		String endAirport;
	}

	// city

	@Data
	private static class CO2aValue {
		double distancea;
		double price1a;
		double price2a;
	}

	@Data
	private static class CO2aKey {
		String startAirporta;
		String endAirporta;
	}

	@Data
	private static class RegionValue {
		String City;
		String Country;
		String Region;
	}

	@Data
	private static class RegionKey {
		String Airport;
	}

	@Data
	private static class MissingKey {
		String missingOrigin;
		String missingDestination;
	}
}
