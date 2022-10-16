<?php
	
	function connexionPDO(){
		$login="root";
		$password="";
		$dbname="zonestockage";
		$serveur="localhost";
		
		try{
			
			$connect = new PDO('mysql:host=localhost;dbname=zonestockage', "root", "");
			return $connect;
		}catch(PDOExeption $e){
			print "Erreur de connexion PDO";
			die();
		}
	}
	
	function getParametre($matricule){
		$connexion=connexionPDO();
		$collectionParametre = array();
		
		$req = "SELECT * From utilisateur where matricule ='$matricule'";
		foreach  ($connexion->query($req) as $row) {
			$collectionParametre[] = $row;
			}
			//var_dump($tableauMedicament[1]['id']);
			$collectionParametreJson = json_encode($collectionParametre);
		
		
		return $collectionParametreJson;
	}

	function getNumUtlisateur($matricule){
		$connexion=connexionPDO();
		
		$sql = "SELECT numUtilisateur From utilisateur where login ='$matricule'";
		$pdostatement = $connexion->query($sql);
		$num = $pdostatement->fetch(PDO::FETCH_ASSOC);
		$strNum=implode($num);
		
		
		return $strNum;
	}
	
	function getMedicament(){
		$connexion=connexionPDO();
		$collectionMedicament = array();
	
		$req="SELECT * from medicament";		
		foreach  ($connexion->query($req) as $row) {
		$collectionMedicament[] = $row;
		}
 
		//var_dump($tableauMedicament[1]['id']);
		$collectionMedicamentJson = json_encode($collectionMedicament);
		
		
		
		return $collectionMedicamentJson;
	}
	
	function suppressionReservation($idReservation){
		$connexion=connexionPDO();
		
		//$idReservation=$_SESSION["idReservation"];
		
		$req="DELETE from reservation where id=$idReservation";
		$pdostatement = $connexion->query($req);
		
		return True;
	}
	

?>