<?php
$insert="INSERT INTO devices (user, idtelefono, password) VALUES ('raelix', 'idphonenumber','mypass')";
$remove="DELETE FROM devices WHERE idtelefono='Griffin' AND user='carmelo'";
$search="SELECT * FROM Persons WHERE FirstName='Peter'";
$getxml = "";
$mode=$_GET['mode'];

switch ($mode) {
    case 1:
      //registrazione
        $usr = $_GET['usr'];
        $idkey = $_GET['idkey'];
        $tel = $_GET['tel'];
        $imei = $_GET['imei'];
        controllaRegistra($usr,$idkey,$tel,$imei);
        break;
    case 2:
     //Invio Sms
        $sender= $_GET['snd'];
        $reciver= $_GET['rcv'];
        $idkey = $_GET['idkey'];
        $msg = $_GET['msg'];
        sendPush($sender,$reciver,$idkey,$msg);
        break;
         case 3:
      //ControlloRegistrazione
         $idkey = $_GET['idkey'];
        break;
    case 4:
      //Notifica Messaggio Ricevuto
          $sender= $_GET['snd'];
        $reciver= $_GET['rcv'];
        $idkey = $_GET['idkey'];
        notificaRicevuta($sender,$reciver,$idkey);
        break;
}

function sendPush($sender,$reciver,$idkey,$msg){
	
	
   if($sender == "" || $sender == "%20" || strlen($sender) < 5){
	   echo 'numero sender valida';
	  return;
	  }
	if($reciver == "" || $reciver == "%20" || strlen($reciver) < 1){
	   echo 'numero reciver non valida';
	  return;
	  }
	  if($idkey == "" || $idkey == "%20" || strlen($idkey) < 1){
	   echo 'chiave non valida';
	  return;
	  }
	
	 $con=mysqli_connect("localhost","root","","testing");
	if (mysqli_connect_errno()){
  echo "Failed to connect to MySQL: ";
  }
	$exist="SELECT * FROM devices ";
	$result = mysqli_query($con,$exist);
	while($row = mysqli_fetch_array($result))
  {  
  $value = $row['idkey'];
  if($value == $idkey){
	  
	  if($row['tel'] == $sender){
		  echo "la tua chiave e' corretta, spedisco il messaggio al: ".$reciver;
		   $con=mysqli_connect("localhost","root","","testing");
	if (mysqli_connect_errno()){
  echo "Failed to connect to MySQL: ";
  }
	$exist="SELECT * FROM devices ";
	$result = mysqli_query($con,$exist);
	while($row = mysqli_fetch_array($result))
  {  
  $rcvphone = $row['tel'];
  if($rcvphone == $sender){
	  $rcvphone = $row['idkey'];
	   sendmessage("ack&2&".$sender."&".$msg,$rcvphone);
	   $ack = "ack&3&";
		  sendmessage($ack.$row['tel'],$idkey);
		  echo "Spedito correttamente";
		  return;
	 }
    }
   }
  }
 }
 }
 
function notificaRicevuta($sender,$reciver,$idkey){
	
	
   if($sender == "" || $sender == "%20" || strlen($sender) < 5){
	   echo 'numero sender valida';
	  return;
	  }
	if($reciver == "" || $reciver == "%20" || strlen($reciver) < 1){
	   echo 'numero reciver non valida';
	  return;
	  }
	  if($idkey == "" || $idkey == "%20" || strlen($idkey) < 1){
	   echo 'chiave non valida';
	  return;
	  }
	
	 $con=mysqli_connect("localhost","root","","testing");
	if (mysqli_connect_errno()){
  echo "Failed to connect to MySQL: ";
  }
	$exist="SELECT * FROM devices ";
	$result = mysqli_query($con,$exist);
	while($row = mysqli_fetch_array($result))
  {  
  $value = $row['idkey'];
  if($value == $idkey){
	  
	  if($row['tel'] == $sender){
		  echo "la tua chiave e' corretta, spedisco il messaggio al: ".$reciver;
		   $con=mysqli_connect("localhost","root","","testing");
	if (mysqli_connect_errno()){
  echo "Failed to connect to MySQL: ";
  }
	$exist="SELECT * FROM devices ";
	$result = mysqli_query($con,$exist);
	while($row = mysqli_fetch_array($result))
  {  
  $rcvphone = $row['tel'];
  if($rcvphone == $sender){
	  $rcvphone = $row['idkey'];
	   sendmessage("ack&4&".$sender,$rcvphone);
		  echo "Notificato correttamente";
		  return;
	 }
    }
   }
  }
 }
 }



function controllaRegistra($usr,$idkey,$tel,$imei){
	
	$con=mysqli_connect("localhost","root","","testing");
	if (mysqli_connect_errno()){
  echo "Failed to connect to MySQL: ";
  }
  
  if($idkey == "" || $idkey == "%20" || strlen($idkey) < 10){
	   echo 'chiave non valida';
	  return;
	  }
	if($usr == "" || $usr == "%20" || strlen($usr) < 1){
	   echo 'nome utente non valido';
	  return;
	  }
	  if($tel == "" || $tel == "%20" || strlen($tel) < 2){
	   echo 'numero di telefono non valido';
	  return;
	  }
	if($imei == "" || $imei == "%20" || strlen($imei) < 10){
	   echo 'imei utente non valido';
	  return;
	  }
	  
	$exist="SELECT * FROM devices ";
	$result = mysqli_query($con,$exist);
	while($row = mysqli_fetch_array($result))
  {  
  $value = $row['idkey'];
  if($value == $idkey){
	  echo 'chiave già memorizzata utente registrato come: '.$row['name'];
		mysqli_close($con);
		return;
		}
	}
	registra($usr,$idkey,$tel,$imei);
	echo "Sei stato registrato correttamente!";
	return;
	}

function registra($usr,$idkey,$tel,$imei){
$con=mysqli_connect("localhost","root","","testing");
$idKey = $idkey;
 $insert="INSERT INTO devices (name, idkey, imei, tel) VALUES ('".$usr."', '".$idkey."','".$imei."','".$tel."')";
$results = mysqli_query($con,$insert);
mysqli_close($con);
$msg = 'ack&1&'.$usr."&".$idKey;
sendmessage($msg,$idKey);
	}

function sendmessage($msg,$idkey){
$registatoin_ids=array($idkey);
$msg=array("message"=>$msg);
$url='https://android.googleapis.com/gcm/send';
$fields=array
 (
  'registration_ids'=>$registatoin_ids,
  'data'=>$msg
 );
$headers=array
 (
  'Authorization: key=AIzaSyDEZDpgDCEIcTX7O3WxOFEtpgi2p5iUDrg',
  'Content-Type: application/json'
 );
$ch=curl_init();
curl_setopt($ch,CURLOPT_URL,$url);
curl_setopt($ch,CURLOPT_POST,true);
curl_setopt($ch,CURLOPT_HTTPHEADER,$headers);
curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
curl_setopt($ch,CURLOPT_SSL_VERIFYPEER,false);
curl_setopt($ch,CURLOPT_POSTFIELDS,json_encode($fields));
$result=curl_exec($ch);
curl_close($ch);	
	}






function doQuery($query){
$con=mysqli_connect("localhost","root","","testing");
if (mysqli_connect_errno()){
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }
$result = mysqli_query($con,$query);
//echo $result;
mysqli_close($con);
}



$rootEl="Devices";			//nome tabella
$childEl="User";				//nome primo figlio
$query="SELECT * FROM dispositivi WHERE user='Peter'" ; //query da restituire in xml
//getXML($rootEl,$childEl,$query);

function getXML($rootElementName,$childElementName,$query){
$Result = "<?xml version='1.0' encoding='utf-8'?>\n";
mysql_connect('localhost', 'root', '');
mysql_select_db('testing');
$queryResult = mysql_query($query);

 $xmlData = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n"; 
    $xmlData .= "<" . $rootElementName . ">\n";
 
    while($record = mysql_fetch_object($queryResult))
    { 
        $xmlData .= "<" . $childElementName . " ";
 
        for ($i = 0; $i < mysql_num_fields($queryResult); $i++)
        { 
            $fieldName = mysql_field_name($queryResult, $i); 
            $xmlData .=  " ".$fieldName . "='";
            if(!empty($record->$fieldName))
                $xmlData .= $record->$fieldName."'"; 
            else
                $xmlData .= "vuoto' " ; 
 
        } 
        $xmlData .= " />\n"; 
    } 
    $xmlData .= "</" . $rootElementName . ">"; 
 
    echo $xmlData; 
}
?>
