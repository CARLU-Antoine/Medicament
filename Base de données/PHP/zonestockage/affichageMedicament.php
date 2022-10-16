<?php
include_once("gestionBase.php");
session_start();


			try{
				$collectionMedicament=getMedicament();		
				
				if(!empty($collectionMedicament)){
					echo($collectionMedicament);
				}else{
					echo("Aucun medicament");
				}

			}catch(PDOExeption $e){
				print "Erreur de connexion PDO";
				die();
			}	

?>