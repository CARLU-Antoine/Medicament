<?php
include_once("gestionBase.php");
session_start();


	try{
		$service=$_REQUEST['service'];
		$matricule=$_REQUEST['matricule'];
		$password=$_REQUEST['password'];
		$type=$_REQUEST['type'];


		if(!empty($service) && !empty($matricule) && !empty($password) && !empty($type)){
		
		
			$connexion=connexionPDO();
	
			
			$req="INSERT INTO `utilisateur` (`service`, `matricule`, `password`, `type`) VALUES ( '$service', '$matricule', '$password', '$type')";
			$prep=$connexion->prepare($req);
			$prep->execute();
			
			if(!empty($prep)){
				echo ("InscriptionOk");
			}else{
				echo ("ErreurInsc");
			}	
		}else{
			echo ("Erreur demande inscription !");
		}
	}catch(PDOExeption $e){
		print "Erreur de connexion PDO";
		die();
	}
?>