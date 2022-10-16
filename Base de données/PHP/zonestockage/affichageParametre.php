<?php
include_once("gestionBase.php");
session_start();


			try{

				$matricule=$_REQUEST['matricule'];

				if(!empty($matricule)){
				$collectionParametre=getParametre($matricule);	
				}	
				
				if(!empty($collectionParametre)){
					echo($collectionParametre);
				}else{
					echo("Aucun Parametre");
				}

			}catch(PDOExeption $e){
				print "Erreur de connexion PDO";
				die();
			}
		

?>