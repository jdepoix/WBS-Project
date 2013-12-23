package dbServices;

import globals.Workpackage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import calendar.DateFunctions;


import jdbcConnection.SQLExecuter;

/**
 * Studienprojekt:	PSYS WBS 2.0<br/>
 * 
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 *			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang<br/>
 * 
 * Diese Klasse dient als Verbindung zur Datenbank fuer das Speicher und Loeschen von Arbeitspaketen.<br/>
 * Zusaetzlich werden hier die Abhaenigkeiten der Arbeitspakete verwaltet.<br/>
 * 
 * @author Sven Seckler, Jens Eckes
 * @version 2.0 - 2012-08-15
 */
public class WorkpackageService {

	/**
	 * Soll aus der konkatenierten hierarchisch ID ein Objekt mit allen vorhandenen Daten machen.
	 * @param stringID ist eine konkatenierten hierarchisch ID eines Arbeitspaketes.
	 * @return Liefert ein bestimmtes Workpackage-Objekt mit allen Daten aus der Datenbank zurueck.
	 */
	public static Workpackage getWorkpackage(String stringID){
		ResultSet resSetMaId = null;
		ResultSet workpackage = null;
		try {
			String query = "SELECT * FROM Arbeitspaket WHERE StringID = '" + stringID + "'";
			workpackage = SQLExecuter.executeQuery(query);
			if(workpackage.next()){
				ArrayList<String> maIds = new ArrayList<String>();	//Eine Liste von zustaendige Mitarbeiter

				//Konstruktor Aufruf des Workpackage mit Initialisierung aus der Datenbank
				Workpackage erg = new Workpackage(workpackage.getInt("LVL1ID"), workpackage.getInt("LVL2ID"),
						workpackage.getInt("LVL3ID"), workpackage.getString("LVLxID"), workpackage.getString("Name"),
						workpackage.getString("Beschreibung"), workpackage.getDouble("CPI"), workpackage.getDouble("BAC"),
						workpackage.getDouble("AC"), workpackage.getDouble("EV"), workpackage.getDouble("ETC"),
						workpackage.getDouble("WP_Tagessatz"), workpackage.getDouble("EAC"), workpackage.getDouble("BAC_Kosten"),
						workpackage.getDouble("AC_Kosten"), workpackage.getDouble("ETC_Kosten"), workpackage.getDate("Release"),
						workpackage.getBoolean("istOAP"), workpackage.getBoolean("istInaktiv"), workpackage.getString("FID_Leiter"),
						maIds, workpackage.getDate("StartdatumRech"),
						workpackage.getDate("StartdatumWunsch"), workpackage.getDate("EnddatumRech"));

				//Fuellen der Liste fuer aller Zustaendigen zu dem Workpackage
				String query2 = "SELECT Paketzuweisung.FID_Ma FROM Paketzuweisung WHERE " +
				"FID_LVL1ID = " + workpackage.getInt("LVL1ID") + " AND FID_LVL2ID = " + workpackage.getInt("LVL2ID") + " AND " +
				"FID_LVL3ID = " + workpackage.getInt("LVL3ID") + " AND FID_LVLxID = '" + workpackage.getString("LVLxID") + "'";
				resSetMaId = SQLExecuter.executeQuery(query2);
				while (resSetMaId.next()){
					maIds.add(resSetMaId.getString("FID_Ma"));
				}
				
				return erg;
			}
			return null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		} finally {
			try {
				if(resSetMaId != null) {
					resSetMaId.close();
				}
				if(workpackage != null) {
					workpackage.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Soll aus einer Set an konkatenierten hierarchisch IDs mehrere Objekt mit allen vorhandenen Daten machen.
	 * @param stringIdSet ist ein Set an konkatenierten hierarchisch ID von Arbeitspaketen.
	 * @return liefert ein Set von Workpackages mit allen Daten aus der Datenbank.
	 */
	public static Set<Workpackage> getWorkpackages(Set<String> stringIdSet){
		Set<Workpackage> workSet = new HashSet<Workpackage>();

		Iterator<String> iterator = stringIdSet.iterator();
		while (iterator.hasNext()) {
			workSet.add(getWorkpackage(iterator.next()));
		}

		return workSet;
	}

	/**
	 * Ein neues Workpackage soll in die Datenbank eingefuegt werden.
	 * @param wp ein neues Workpackage was in die Datenbank eingetragen werde soll.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean insertWorkpackage(Workpackage wp) {
		try {
			String query = "INSERT INTO Arbeitspaket (StringID, FID_Proj, LVL1ID, LVL2ID, LVL3ID, LVLxID, FID_Leiter, Name, Beschreibung, BAC, AC, " +
			"EV, ETC, EAC, CPI, BAC_Kosten, AC_Kosten, ETC_Kosten, WP_Tagessatz, Release, istOAP, istInaktiv, StartdatumWunsch)" +
			"VALUES ('" + getStringIdFromWp(wp) + "', 1, " + wp.getLvl1ID() + ", " + wp.getLvl2ID() + ", " + wp.getLvl3ID() + ", '" + wp.getLvlxID() + "', '" +
			wp.getFid_Leiter() + "', '" + wp.getName() + "', '" + wp.getBeschreibung() + "', " + wp.getBac() + "," + wp.getAc() + ", " +
			wp.getEv() + ", " + wp.getEtc() + ", " + wp.getEac() + ", " + wp.getCpi() + ", " + wp.getBac_kosten() + ", " +
			wp.getAc_kosten() + ", " + wp.getEtc_kosten() + ", " + wp.getWptagessatz() + ", " + DateFunctions.getDateString(wp.getEndDateCalc()) + ", " +
			wp.isIstOAP() + ", " + wp.isIstInaktiv() + ", "+
			DateFunctions.getDateString(wp.getStartDateHope())+ ")";
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Ein geaendertes Workpackage soll in die Datenbank uebernommen werden.
	 * @param wp ein Workpackage dessen Aenderungen in der Datenbank uebernommen werden sollen.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean updateWorkpackage(Workpackage wp) {
		String query = "";
		try {
			query =  "UPDATE Arbeitspaket " +
			"SET Name = '" + wp.getName() + "'" +
			", Beschreibung = '" + wp.getBeschreibung() + "'" +
			", CPI = " + wp.getCpi() + 
			", BAC = " + wp.getBac() + 
			", AC = " + wp.getAc() +
			", EV = " + wp.getEv() + 
			", ETC = " + wp.getEtc() + 
			", WP_Tagessatz = " + wp.getWptagessatz() +
			", EAC = " + wp.getEac() + 
			", BAC_Kosten = " + wp.getBac_kosten() +
			", AC_Kosten = " + wp.getAc_kosten() + 
			", ETC_Kosten = " + wp.getEtc_kosten() +
			", Release = " + DateFunctions.getDateString(wp.getEndDateHope()) + 
			", istOAP = " + wp.isIstOAP() + 
			", istInaktiv = " + wp.isIstInaktiv() + 
			", FID_Leiter = '" + wp.getFid_Leiter() + "'" +
			", StartdatumRech = " + DateFunctions.getDateString(wp.getStartDateCalc()) +
			", StartdatumWunsch = " + DateFunctions.getDateString(wp.getStartDateHope()) + 
			", EnddatumRech = " + DateFunctions.getDateString(wp.getEndDateCalc()) +
			" WHERE StringID = '" + getStringIdFromWp(wp) + "'";
			
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.err.println(query);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Ein zu loeschendes Workpackage soll aus der Datenbank entfernt werden.
	 * @param wp ein Workpackage das aus der Datenbank entfernt werden soll.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean deleteWorkpackage(Workpackage wp) {
		try {
			String query = "DELETE FROM Arbeitspaket WHERE StringID = '" + getStringIdFromWp(wp) + "'";
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ### Wird nur noch zum Testen bei testService/TestWorkpackageService.java verwendet
	 * Liefert ein Set von Workpackage-Objekte die Nachfolgern eines bestimmten Arbeitspaketes sind.
	 * @param stringID ein konkatenierten hierarchisch ID eines Arbeitspaketes.
	 * @return Set<Workpackage> liefert ein Set von Vorgaenger-Workpackages mit allen Daten aus der Datenbank.
	 */
	@Deprecated
	public static Set<Workpackage> getFollowers(String stringID){
		return getConnection(stringID, "FID_Nachfolger", "FID_Vorgaenger");
	}

	/**
	 * ### Wird nur noch zum Testen bei testService/TestWorkpackageService.java verwendet
	 * Liefert ein Set von Workpackage-Objekte die Vorgaenger eines bestimmten Arbeitspaketes sind.
	 * @param stringID ein konkatenierten hierarchisch ID eines Arbeitspaketes.
	 * @return Set<Workpackage> liefert ein Set von Nachfolger-Workpackages mit allen Daten aus der Datenbank.
	 */
	@Deprecated
	public static Set<Workpackage> getAncestors(String stringID) {
		return getConnection(stringID, "FID_Vorgaenger", "FID_Nachfolger");
	}

	/**
	 * Liefert alle Abhaengigkeiten von Arbeitspaketen zu ihren Vorgaenger
	 * @return Map<String, Set<String>> Alle Arbeitspaket zu ihren Vorgaenger
	 */
	public static Map<String, Set<String>> getFollowerToAncestorsIdMap(){
		return getConnectionIdMap("FID_Nachfolger", "FID_Vorgaenger");
	}

	/**
	 * Liefert alle Abhaengigkeiten von Arbeitspaketen zu ihren Nachfolgern
	 * @return Map<String, Set<String>> Alle Arbeitspaket zu ihren Nachfolgern
	 */
	public static Map<String, Set<String>> getAncestorToFollowersIdMap() {
		return getConnectionIdMap("FID_Vorgaenger", "FID_Nachfolger");
	}

	/**
	 * Speichert einen Nachfolger fuer ein bestimmtes Arbeitspaket.
	 * @param thisAP StringID des Arbeitspaketes fuer das der Nachfolger gesetzt werden soll.
	 * @param followerAP StringID des Nachfolgers welcher gespeichert werden soll.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean setFollower(String thisAP, String followerAP){
		try {
			String query = "INSERT INTO Abhaengigkeiten (FID_Vorgaenger, FID_Nachfolger) VALUES ('" + thisAP  + "', '" + followerAP + "')";
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Speichert einen Vorgaenger fuer ein bestimmtes Arbeitspaket.
	 * @param thisAP StringID des Arbeitspaketes fuer das der Vorgaenger gesetzt werden soll.
	 * @param followerAP StringID des Vorgaengers welcher gespeichert werden soll.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean setAncestor(String thisAP, String ancestorAP){
		return setFollower(ancestorAP, thisAP);
	}

	/**
	 * Loeschten einer bestimmten Nachfolger-Beziehung zwischen zwei Arbeitspaketen.
	 * @param thisAP ist die StringID eines bestimmten Arbeitspaketes.
	 * @param followerAP ist die StringID eines bestimmten Nachfolgers.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean deleteFollower(String thisAP, String followerAP) {
		try {
			String query = "DELETE FROM Abhaengigkeiten WHERE FID_Vorgaenger = '" + thisAP + "'" +
			" AND FID_Nachfolger = '" + followerAP + "'";
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Loeschten einer bestimmten Vorgaenger-Beziehung zwischen zwei Arbeitspaketen.
	 * @param thisAP ist die StringID eines bestimmten Arbeitspaketes.
	 * @param followerAP ist die StringID eines bestimmten Vorgaenger.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean deleteAncestor(String thisAP, String ancestorAP) {
		return deleteFollower(ancestorAP, thisAP);
	}

	/**
	 * ### Abhaengigkeiten duerfen bisher nur einzeln geloescht werden.
	 * Loescht alle Abhaengigkeiten eines Arbeitspaketes
	 * @param thisAP ist die generierte ID des Arbeitspaketes was keine Abhaengigkeit mehr haben soll
	 */
	@Deprecated
	public static boolean deleteConnections(String thisAP){
		return deleteAncestors(thisAP) && deleteFollowers(thisAP);
	}

	/**
	 * Soll in der DB.Paketzuweisung einen Arbeiter einem Paket zuweisen.
	 * @param wp ein Workpackage dem ein Arbeiter zugewiesen werden soll.
	 * @param workerID der Login eines neuen Arbeiters.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean addWpWorker(Workpackage wp, String workerID){
		try {
			String query = "INSERT INTO Paketzuweisung (FID_Proj, FID_LVL1ID, FID_LVL2ID, FID_LVL3ID, FID_LVLxID, FID_Ma) " +
			"VALUES( 1 ,"+wp.getLvl1ID() + ", " + wp.getLvl2ID() + ", " + wp.getLvl3ID() + 
			", '" + wp.getLvlxID() + "', '" + workerID + "')";
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Soll einen Arbeiter aus DB.Paketzuweisung entfernen.
	 * @param wp ein Workpackage dem ein Arbeiter entfernt werden soll.
	 * @param workerID der Login eines Arbeiters.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean removeWpWorker(Workpackage wp, String workerID){
		try {
			String query = "DELETE FROM Paketzuweisung " +
			"WHERE FID_Proj = 1 " +
			" AND FID_LVL1ID = " + wp.getLvl1ID() + 
			" AND FID_LVL2ID = " + wp.getLvl2ID() +
			" AND FID_LVL3ID = " + wp.getLvl3ID() +
			" AND FID_LVLxID = '" + wp.getLvlxID() + "'" +
			" AND FID_Ma = '" + workerID + "'";
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Generiert aus einem Workpackage seine StringID
	 * @param wp das betroffen Workpackage.
	 * @return Gibt die eindeutige StringID zurueck. 
	 */
	private static String getStringIdFromWp(Workpackage wp){
		String stringID = "";
		stringID += wp.getLvl1ID() + ".";
		stringID += wp.getLvl2ID() + ".";
		stringID += wp.getLvl3ID() + ".";
		stringID += wp.getLvlxID();
		return stringID;
	}

	/**
	 * ### Wird nur noch zum Testen bei testService/TestWorkpackageService.java verwendet
	 * Sucht nach den mitgegebenen Abhaengigkeiten von einer StringID
	 * @param stringID gibt die StingID nach der gesucht wird an.
	 * @param select gibt den Namen der Spalte mit den Informationen an.
	 * @param where gibt den Namen der Spalten von stringID an.
	 * @return Set<Workpackage> mit allen Objekten in der mitgegebenen Abhaengigkeit.
	 */
	@Deprecated
	private static Set<Workpackage> getConnection(String stringID, String select, String where){
		try {
			String query = "SELECT " + select + " FROM Abhaengigkeiten WHERE " + where + " = '" + stringID + "'";
			ResultSet resSet = SQLExecuter.executeQuery(query);

			Set<String> ergSet = new HashSet<String>();
			while(resSet.next()){
				ergSet.add(resSet.getString(select));
			}
			resSet.close();
			Set<Workpackage> wpSet = getWorkpackages(ergSet);

			return wpSet;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sucht nach allen Abhaengigkeiten von first zu second
	 * @param first gibt den Namen der Spalte mit den einmaligen IDs.
	 * @param second gibt den Namen der Spalte mit der Abhaengigkeit.
	 * @return Set<Workpackage> mit allen Objekten in der mitgegebenen Abhaengigkeit.
	 */
	private static Map<String, Set<String>> getConnectionIdMap(String first, String second){
		try {
			String query = "SELECT * FROM Abhaengigkeiten";
			ResultSet resSet = SQLExecuter.executeQuery(query);
			Map<String, Set<String>> ergMap = new TreeMap <String, Set<String>>();
			while(resSet.next()){
				String followerKey = resSet.getString(first);
				if (!ergMap.containsKey(followerKey)){
					ergMap.put(followerKey, new HashSet<String>());
				}
				ergMap.get(followerKey).add(resSet.getString(second));
			}
			resSet.close();
			return ergMap;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ### Vorgaenger duerfen bisher nur einzeln geloescht werden.
	 * Loescht alle Nachfolger-Beziehung eines Arbeitspaketes
	 * @param thisAP StringID eines Arbeitspaketes was keine Nachfolger mehr haben soll.
	 */
	@Deprecated
	private static boolean deleteFollowers(String thisAP){
		return deleteConnections(thisAP, "FID_Vorgaenger");
	}

	/**
	 * ### Vorgaenger duerfen bisher nur einzeln geloescht werden.
	 * Loescht alle Vorgaenger-Beziehung eines Arbeitspaketes
	 * @param thisAP StringID eines Arbeitspaketes was keine Nachfolger mehr haben soll.
	 */
	@Deprecated
	private static boolean deleteAncestors(String thisAP){
		return deleteConnections(thisAP, "FID_Nachfolger");
	}

	/**
	 * ### Vorgaenger duerfen bisher nur einzeln geloescht werden.
	 * Loescht alle Beziehung eines Arbeitspaketes in der mitgegebenen Abhaengigkeit.
	 * @param thisAP StringID eines Arbeitspaketes was eine Beziehung verlieren soll.
	 * @param where Beziehung die geloescht werden soll.
	 */
	@Deprecated
	private static boolean deleteConnections(String thisAP, String where){
		try {
			String query = "DELETE FROM Abhaengigkeiten WHERE " + where + " = " + thisAP;
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Liefert ein Set mit allen Arbeitsparketen des Projekts.
	 * @return Set<Workpackage> alle Arbeitsparkete
	 */
	static public Set<Workpackage> getAllAp(){
		Set<Workpackage> returnSet = null;
		try {
			returnSet = WorkpackageService.fillWps(new HashSet<Workpackage>(), SQLExecuter.executeQuery("SELECT * FROM Arbeitspaket"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnSet;
	}

	/**
	 * Liefert ein Set mit allen Arbeitsparketen des Projekts in einem bestimmten Zeitraum.
	 * @param from Startdatum des Zeitraums
	 * @param to Enddatum des Zeitraums
	 * @return Set<Workpackage> alle Arbeitsparkete
	 */
	static public Set<Workpackage> getAllAp(Date from, Date to){
		Set<Workpackage> returnSet = null;
		try {
			returnSet = WorkpackageService.fillWps(new HashSet<Workpackage>(), SQLExecuter.executeQuery("SELECT * FROM Arbeitspaket WHERE StartdatumRech BETWEEN "
					+ DateFunctions.getDateString(from) + " AND " + DateFunctions.getDateString(to) + " OR EnddatumRech BETWEEN " + DateFunctions.getDateString(from) + " AND " + DateFunctions.getDateString(to) ));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnSet;
	}

	/**
	 * Liefert ein Set mit allen Arbeitsparketen, die einem bestimmten Mitarbeiter zugewiesen sind.
	 * @param workerID ID des Mitarbeiters fuer den alle Arbeitsparkete geladen werden soll.
	 * @return Set<Workpackage> alle Arbeitsparkete
	 */
	static public Set<Workpackage> getAllWorkerAp(String workerID){
		Set<Workpackage> returnSet = null;
		try {
			returnSet = WorkpackageService.fillWps(new HashSet<Workpackage>(), SQLExecuter.executeQuery("SELECT * " +
					"FROM Paketzuweisung pzw INNER JOIN Arbeitspaket ap ON pzw.FID_LVL1ID = ap.LVL1ID AND pzw.FID_LVL2ID = ap.LVL2ID AND pzw.FID_LVL3ID = ap.LVL3ID AND pzw.FID_LVLxID = ap.LVLxID " +
					"WHERE pzw.FID_Ma = '" + workerID + "'"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnSet;
	}

	/**
	 * Liefert ein Set mit allen Arbeitsparketen, die einem bestimmten Mitarbeiter zugewiesen sind und
	 * in einem bestimmten Zeitraum liegen.
	 * @param workerID ID des Mitarbeiters fuer den alle Arbeitsparkete geladen werden soll.
	 * @param from Startdatum des Zeitraums
	 * @param to Enddatum des Zeitraums
	 * @return Set<Workpackage> alle Arbeitsparkete
	 */
	static public Set<Workpackage> getAllWorkerAp(String workerID, Date from, Date to){
		Set<Workpackage> returnSet = null;
		try {
			returnSet = WorkpackageService.fillWps(new HashSet<Workpackage>(),SQLExecuter.executeQuery("SELECT * " +
					"FROM Paketzuweisung pzw INNER JOIN Arbeitspaket ap ON pzw.FID_LVL1ID = ap.LVL1ID AND pzw.FID_LVL2ID = ap.LVL2ID AND pzw.FID_LVL3ID = ap.LVL3ID AND pzw.FID_LVLxID = ap.LVLxID " +
					"WHERE pzw.FID_Ma = '" + workerID + "' AND ap.EnddatumRech >= " + DateFunctions.getDateString(from) + " AND ap.StartdatumRech <= " + DateFunctions.getDateString(to)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnSet;
	}

	/**
	 * Diese Methode fuellt ein Set von Workpackages mit den Daten eines ResultSets
	 * @param workSet das zu fuellende Set vom Typ Workpackage
	 * @param resSet das ResultSet mit daten aus der Datenbanl
	 * @return ein Set vom Typ Workpackage 
	 * @throws SQLException 
	 */
	static private Set<Workpackage> fillWps(Set<Workpackage> workSet, ResultSet resSet) throws SQLException{
		ArrayList<String> maIds = null;
		ResultSet resSetMaId = null;
		while (resSet.next()) {
			maIds = new ArrayList<String>();
			
			int lvl1ID = resSet.getInt("LVL1ID");
			int lvl2ID = resSet.getInt("LVL2ID");
			int lvl3ID = resSet.getInt("LVL3ID");
			String lvlxID =  resSet.getString("LVLxID");
			
			
			String sql = "SELECT Paketzuweisung.FID_Ma FROM Paketzuweisung WHERE FID_LVL1ID = " + lvl1ID + " AND FID_LVL2ID = " 
			+ lvl2ID + " AND FID_LVL3ID = " + lvl3ID + " AND FID_LVLxID = '" + lvlxID+"'";
			
			resSetMaId = SQLExecuter.executeQuery(sql);
			
			while (resSetMaId.next()){
				maIds.add(resSetMaId.getString("FID_Ma"));
			}
			workSet.add(new Workpackage(lvl1ID, lvl2ID, lvl3ID, lvlxID,
					resSet.getString("Name"), resSet.getString("Beschreibung"), new Double(resSet.getDouble("CPI")), new Double(resSet.getDouble("BAC")), 
					resSet.getDouble("AC"), resSet.getDouble("EV"), resSet.getDouble("ETC"), resSet.getDouble("WP_Tagessatz"), resSet.getDouble("EAC"), 
					resSet.getDouble("BAC_Kosten"), resSet.getDouble("AC_Kosten"), resSet.getDouble("ETC_Kosten"), resSet.getDate("Release"), 
					resSet.getBoolean("istOAP"), resSet.getBoolean("istInaktiv"), resSet.getString("FID_Leiter"), 
					maIds, resSet.getDate("StartdatumRech"), 
					resSet.getDate("StartdatumWunsch"), resSet.getDate("EnddatumRech")));
			maIds = null;
			resSetMaId.getStatement().close();
		}
		resSet.getStatement().close();
	
		return workSet;
	}
}