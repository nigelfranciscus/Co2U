import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
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

		try {
			List<String[]> allRows;
			List<String[]> allRowsa;
			allRows = parser.parseAll(new FileReader("C:/Users/s2876731/Desktop/co2sample.csv"));
			allRowsa = parser.parseAll(new FileReader("C:/Users/s2876731/Desktop/city.csv"));

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

			BufferedReader lineReader = new BufferedReader(new FileReader(
					"C:/Users/s2876731/Documents/AIRtravel/New Monthly/itineraries_griffith_2016m02.csv"));
			FileWriter writer = new FileWriter("C:/Users/s2876731/Documents/AIRtravel/New Monthly/Results/2016m02.csv");

			writer.write("year" + "," + "month" + "," + "origin_airport" + "," + "CX1_AIRPORT" + "," + "CX2_AIRPORT"
					+ "," + "destination_airport" + "," + "airline_1" + "," + "airline_2" + "," + "airline_3" + ","
					+ "CO2P1" + "," + "CO2P2" + "," + "CO2P3" + "," + "CO2E1" + "," + "CO2E2" + "," + "CO2E3" + ","
					+ "SUMCO2P" + "," + "SUMCO2E" + "," + "pax" + "," + "pax_in_first_class" + ","
					+ "pax_in_business_class" + "," + "pax_in_economy_class" + "," + "origin_city" + ","
					+ "origin_country" + "," + "origin_region" + "," + "cx1_city" + "," + "cx1_country" + ","
					+ "cx1_region" + "," + "distance_1" + "," + "cx2_city" + "," + "cx2_country" + "," + "cx2_region"
					+ "," + "distance_2" + "," + "destination_city" + "," + "destination_country" + ","
					+ "destination_region" + "," + "distance_3" + "," + "main_airline" + "," + "SUMDISTANCE" + ","
					+ "DIRECTDISTANCE");
			writer.write("\n");

			String newLine;
			while ((newLine = lineReader.readLine()) != null) {
				String[] row = newLine.split(",");

				if (row[0] == null || row[0].length() == 0 || row[2] == null || row[2].length() == 0 || row[5] == null || row[5].length() == 0) {
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
				 * Handle when the third cell is empty, the fourth is definitely empty
				 */
				if(row[3] == null || row[3].length() == 0){
					
					CO2Key co2Key = new CO2Key();
					co2Key.setStartAirport(row[2]);
					co2Key.setEndAirport(row[5]);
					
					CO2Value co2Value = co2List.get(co2Key);
					if(co2Value != null){
						co2Out.getDISTs()[2] = co2Value.getDistance();
						co2Out.getCO2Es()[2] = co2Value.getPrice1();
						co2Out.getCO2Ps()[2] = co2Value.getPrice2();
					}else{
						CO2aKey co2Keya = new CO2aKey();
						co2Keya.setStartAirporta(row[13]);
						co2Keya.setEndAirporta(row[24]);
						
						CO2aValue co2aValue = co2aList.get(co2Keya);
						if(co2aValue != null){
							co2Out.getDISTs()[2] = co2aValue.getDistancea();
							co2Out.getCO2Es()[2] = co2aValue.getPrice1a();
							co2Out.getCO2Ps()[2] = co2aValue.getPrice2a();
						}
						else{							
							co2Out.getCO2Es()[2] = (Double.valueOf(row[27])*0.1);
							co2Out.getCO2Ps()[2] = (Double.valueOf(row[27])*0.1);							
						}
					}
				}
				/*
				 * Handle when the third cell is not empty, but the fourth is empty
				 */
				if(row[3].length() > 0 &&  (row[4] == null || row[4].length() == 0)){
					/*
					 * Set the values for the first cell of values
					 */
					CO2Key co2Key = new CO2Key();
					co2Key.setStartAirport(row[2]);
					co2Key.setEndAirport(row[3]);
					
					CO2Value co2Value = co2List.get(co2Key);
					if(co2Value != null){
						co2Out.getDISTs()[0] = co2Value.getDistance();
						co2Out.getCO2Es()[0] = co2Value.getPrice1();
						co2Out.getCO2Ps()[0] = co2Value.getPrice2();
					}else{
						CO2aKey co2Keya = new CO2aKey();
						co2Keya.setStartAirporta(row[13]);
						co2Keya.setEndAirporta(row[16]);
						
						CO2aValue co2aValue = co2aList.get(co2Keya);
						if(co2aValue != null){
							co2Out.getDISTs()[0] = co2aValue.getDistancea();
							co2Out.getCO2Es()[0] = co2aValue.getPrice1a();
							co2Out.getCO2Ps()[0] = co2aValue.getPrice2a();
						}
						else{
							if(row[19] != null && row[19].length() > 0){
							co2Out.getCO2Es()[0] = (Double.valueOf(row[19])*0.1);
							co2Out.getCO2Ps()[0] = (Double.valueOf(row[19])*0.1);
							}
						}
					}
					/*
					 * Set the values for the third cell of values
					 */
					co2Key.setStartAirport(row[3]);
					co2Key.setEndAirport(row[5]);
					co2Value = co2List.get(co2Key);
					if(co2Value != null){
						co2Out.getDISTs()[2] = co2Value.getDistance();
						co2Out.getCO2Es()[2] = co2Value.getPrice1();
						co2Out.getCO2Ps()[2] = co2Value.getPrice2();
					}else{
						CO2aKey co2Keya = new CO2aKey();
						co2Keya.setStartAirporta(row[16]);
						co2Keya.setEndAirporta(row[24]);
						
						CO2aValue co2aValue = co2aList.get(co2Keya);
						if(co2aValue != null){
							co2Out.getDISTs()[2] = co2aValue.getDistancea();
							co2Out.getCO2Es()[2] = co2aValue.getPrice1a();
							co2Out.getCO2Ps()[2] = co2aValue.getPrice2a();
						}
						else{
							if(row[27] != null && row[27].length() > 0){
							co2Out.getCO2Es()[2] = (Double.valueOf(row[27])*0.1);
							co2Out.getCO2Ps()[2] = (Double.valueOf(row[27])*0.1);
							}
						}
					}
				}
				
				if(row[4].length() > 0){
					/*
					 * Set the values for the first cell of values
					 */
					CO2Key co2Key = new CO2Key();
					co2Key.setStartAirport(row[2]);
					co2Key.setEndAirport(row[3]);
					
					CO2Value co2Value = co2List.get(co2Key);
					if(co2Value != null){
						co2Out.getDISTs()[0] = co2Value.getDistance();
						co2Out.getCO2Es()[0] = co2Value.getPrice1();
						co2Out.getCO2Ps()[0] = co2Value.getPrice2();
					}else{
						CO2aKey co2Keya = new CO2aKey();
						co2Keya.setStartAirporta(row[13]);
						co2Keya.setEndAirporta(row[16]);
						
						CO2aValue co2aValue = co2aList.get(co2Keya);
						if(co2aValue != null){
							co2Out.getDISTs()[0] = co2aValue.getDistancea();
							co2Out.getCO2Es()[0] = co2aValue.getPrice1a();
							co2Out.getCO2Ps()[0] = co2aValue.getPrice2a();
						}
						else{
							co2Out.getCO2Es()[0] = (Double.valueOf(row[19])*0.1);
							co2Out.getCO2Ps()[0] = (Double.valueOf(row[19])*0.1);
						}
					}
					/*
					 * Set the values for the third cell of values
					 */
					co2Key.setStartAirport(row[3]);
					co2Key.setEndAirport(row[4]);
					co2Value = co2List.get(co2Key);
					if(co2Value != null){
						co2Out.getDISTs()[1] = co2Value.getDistance();
						co2Out.getCO2Es()[1] = co2Value.getPrice1();
						co2Out.getCO2Ps()[1] = co2Value.getPrice2();
					}else{
						CO2aKey co2Keya = new CO2aKey();
						co2Keya.setStartAirporta(row[16]);
						co2Keya.setEndAirporta(row[20]);
						
						CO2aValue co2aValue = co2aList.get(co2Keya);
						if(co2aValue != null){
							co2Out.getDISTs()[1] = co2aValue.getDistancea();
							co2Out.getCO2Es()[1] = co2aValue.getPrice1a();
							co2Out.getCO2Ps()[1] = co2aValue.getPrice2a();
						}
						else{
							co2Out.getCO2Es()[1] = (Double.valueOf(row[23])*0.1);
							co2Out.getCO2Ps()[1] = (Double.valueOf(row[23])*0.1);
						}
					}
					/*
					 * Set the values for the third cell of values
					 */
					co2Key.setStartAirport(row[4]);
					co2Key.setEndAirport(row[5]);
					co2Value = co2List.get(co2Key);
					if(co2Value != null){
						co2Out.getDISTs()[2] = co2Value.getDistance();
						co2Out.getCO2Es()[2] = co2Value.getPrice1();
						co2Out.getCO2Ps()[2] = co2Value.getPrice2();
					}else{
						CO2aKey co2Keya = new CO2aKey();
						co2Keya.setStartAirporta(row[20]);
						co2Keya.setEndAirporta(row[24]);
						
						CO2aValue co2aValue = co2aList.get(co2Keya);
						if(co2aValue != null){
							co2Out.getDISTs()[2] = co2aValue.getDistancea();
							co2Out.getCO2Es()[2] = co2aValue.getPrice1a();
							co2Out.getCO2Ps()[2] = co2aValue.getPrice2a();
						}
						else{
							if(row[27] != null && row[27].length() > 0){
							co2Out.getCO2Es()[2] = (Double.valueOf(row[27])*0.1);
							co2Out.getCO2Ps()[2] = (Double.valueOf(row[27])*0.1);
							}
						}
					}
				}



				double number1 = 0, number2 = 0, number3 = 0;
				if (row[19] != null && row[19].trim().length() != 0) {
					number1 = Double.valueOf(row[19]);
				}
				if (row[23] != null && row[23].trim().length() != 0) {
					number2 = Double.valueOf(row[23]);
				}
				if (row[27] != null && row[27].trim().length() != 0) {
					number3 = Double.valueOf(row[27]);
				}

				co2Out.setACTUALSUM(number1 + number2 + number3);
				co2Out.setSUM(co2Out.getDISTs()[0] + co2Out.getDISTs()[1] + co2Out.getDISTs()[2]);
				co2Out.setSUMCO2E(co2Out.getCO2Es()[0] + co2Out.getCO2Es()[1] + co2Out.getCO2Es()[2]);
				co2Out.setSUMCO2P(co2Out.getCO2Ps()[0] + co2Out.getCO2Ps()[1] + co2Out.getCO2Ps()[2]);

				// Direct Distance
				CO2Key co2Key = new CO2Key();
				CO2aKey co2aKey = new CO2aKey();

				co2Key.setStartAirport(row[2]);
				co2Key.setEndAirport(row[5]);

				co2aKey.setStartAirporta(row[13]);
				co2aKey.setEndAirporta(row[24]);

				CO2Value value = co2List.get(co2Key);
				CO2aValue valuea = co2aList.get(co2aKey);
				if (value != null) {
					co2Out.setDIRECT(value.getDistance());
				} else if(valuea != null) {
					co2Out.setDIRECT(valuea.getDistancea());
				}

				co2Out.setYEAR(row[0]);
				co2Out.setMONTH(row[1]);

				if (row[9] != null && row[9].length() > 0) {
					co2Out.setPAX(Double.valueOf(row[9]));
				} else {
					co2Out.setPAX(0);
				}
				// co2Out.setPAX(Double.valueOf(row[9]));
				co2Out.setPAX_FIRST(Double.valueOf(row[10]));
				co2Out.setPAX_BUS(Double.valueOf(row[11]));
				co2Out.setPAX_ECO(Double.valueOf(row[12]));

				co2Out.setORIGIN_CITY(row[13] != null ? row[13] : "");
				co2Out.setORIGIN_COUNTRY(row[14] != null ? row[14] : "");
				co2Out.setORIGIN_REGION(row[15] != null ? row[15] : "");

				co2Out.setCX1_CITY(row[16] != null ? row[16] : "");
				co2Out.setCX1_COUNTRY(row[17] != null ? row[17] : "");
				co2Out.setCX1_REGION(row[18] != null ? row[18] : "");
				if (row[19] != null && row[19].length() > 0) {
					co2Out.setDEST1(Double.valueOf(row[19]));
				} else {
					co2Out.setDEST1(0);
				}

				co2Out.setCX2_CITY(row[20] != null ? row[20] : "");
				co2Out.setCX2_COUNTRY(row[21] != null ? row[21] : "");
				co2Out.setCX2_REGION(row[22] != null ? row[22] : "");
				if (row[23] != null && row[23].length() > 0) {
					co2Out.setDEST2(Double.valueOf(row[23]));
				} else {
					co2Out.setDEST2(0);
				}

				co2Out.setDEST_CITY(row[24] != null ? row[24] : "");
				co2Out.setDEST_COUNTRY(row[25] != null ? row[25] : "");
				co2Out.setDEST_REGION(row[26] != null ? row[26] : "");
				if (row[27] != null && row[27].length() > 0) {
					co2Out.setDEST3(Double.valueOf(row[27]));
				} else {
					co2Out.setDEST3(0);
				}

				co2Out.setMAIN_AIR(row[28] != null ? row[28] : "");

				writer.append(co2Out.getYEAR() + ",");
				writer.append(co2Out.getMONTH() + ",");
				writer.append(co2Out.getDEP() + ",");
				writer.append(co2Out.getCON1() + ",");
				writer.append(co2Out.getCON2() + ",");
				writer.append(co2Out.getARR() + ",");
				writer.append(co2Out.getFLIGHT1() + ",");
				writer.append(co2Out.getFLIGHT2() + ",");
				writer.append(co2Out.getFLIGHT3() + ",");

				// for (int i = 0; i < 3; i++) {
				// writer.append(co2Out.getDISTs()[i] + ",");
				// }
				// writer.append(co2Out.getSUM() + ",");
				for (int i = 0; i < 3; i++) {
					writer.append(co2Out.getCO2Ps()[i] + ",");
				}
				for (int i = 0; i < 3; i++) {
					writer.append(co2Out.getCO2Es()[i] + ",");
				}
				writer.append(co2Out.getSUMCO2P() + ",");
				writer.append(co2Out.getSUMCO2E() + ",");
				// writer.append(co2Out.getDIST() + ",");
				writer.append(co2Out.getPAX() + ",");
				writer.append(co2Out.getPAX_FIRST() + ",");
				writer.append(co2Out.getPAX_BUS() + ",");
				writer.append(co2Out.getPAX_ECO() + ",");
				writer.append(co2Out.getORIGIN_CITY() + ",");
				writer.append(co2Out.getORIGIN_COUNTRY() + ",");
				writer.append(co2Out.getORIGIN_REGION() + ",");
				writer.append(co2Out.getCX1_CITY() + ",");
				writer.append(co2Out.getCX1_COUNTRY() + ",");
				writer.append(co2Out.getCX1_REGION() + ",");
				writer.append(co2Out.getDEST1() + ",");
				writer.append(co2Out.getCX2_CITY() + ",");
				writer.append(co2Out.getCX2_COUNTRY() + ",");
				writer.append(co2Out.getCX2_REGION() + ",");
				writer.append(co2Out.getDEST2() + ",");
				writer.append(co2Out.getDEST_CITY() + ",");
				writer.append(co2Out.getDEST_COUNTRY() + ",");
				writer.append(co2Out.getDEST_REGION() + ",");
				writer.append(co2Out.getDEST3() + ",");
				writer.append(co2Out.getMAIN_AIR() + ",");
				writer.append(co2Out.getACTUALSUM() + ",");
				writer.append(co2Out.getDIRECT() + "");
				writer.append("\n");

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
			lineReader.close();
			writer.flush();
			writer.close();
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

		CO2Out() {
			DEP = null;
			DISTs = new double[3];
			CO2Ps = new double[3];
			CO2Es = new double[3];
		}

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
}
