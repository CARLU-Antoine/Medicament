<?php
include_once("gestionBase.php");
session_start();


	try{
		$matricule=$_REQUEST['matricule'];
		$libelle=$_REQUEST['libelle'];
		$prix=$_REQUEST['prix'];
		$quantite=$_REQUEST['quantite'];



		if(!empty($matricule) && !empty($libelle) && !empty($prix) && !empty($quantite)){
		
		
			$connexion=connexionPDO();
	
			$req_matricule="SELECT numUtilisateur from utilisateur where matricule='$matricule';";
			$prepaMatricule = $connexion->prepare($req_matricule);
			$prepaMatricule->execute();
			$numMatriculeStocke = $prepaMatricule->fetch();

			$numMatricule = $numMatriculeStocke['numUtilisateur'];


			
			$req="INSERT INTO `medicament` ( `libelle`, `prix`, `quantite`, `matricule`) VALUES ('$libelle', '$prix', '$quantite', '$numMatricule');";
			echo $req;
			$prep=$connexion->prepare($req);
			$prep->execute();
			
			if(!empty($prep)){
				echo ("MedicamentOk");
			}else{
				echo ("ErreurMed");
			}	
		}else{
			echo ("Erreur Insertion Medicament !");
		}
	}catch(PDOExeption $e){
		print "Erreur de connexion PDO";
		die();
	}
?>