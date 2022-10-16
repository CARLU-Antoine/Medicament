<?php
include("gestionBase.php");
session_start();
		try{
			$connexion=connexionPDO();
			
			$matricule=$_REQUEST['matricule'];
			$password=$_REQUEST['password'];
				
			if(!empty($matricule) && !empty($password)){
				
				$req="SELECT count(*) as nb From utilisateur where matricule='$matricule' and password ='$password'";

				$prep=$connexion->prepare($req);
				$prep->execute();
		
				$resultat = $prep->fetch();
						
				if($resultat['nb']==1){
					echo('Authentification');
				}else{
					echo('Aucune Authentification');
				}					
			}else{
				echo ("Erreur Authentification !");
			}
		}catch(PDOExeption $e){
			print "Erreur de connexion PDO";
			die();
		}		
?>
