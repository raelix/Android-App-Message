<?php
$regID=$_GET['id'];
$user=$_GET['usr'];
$msg='sei stato aggiunto correttamente';
$dom = new DomDocument;
$dom->load('C:\Program Files\Apache Software Foundation\Tomcat 7.0\users.xml');
foreach( $dom->getElementsByTagName( 'User' ) as $file ) {
    if( $file->getAttribute('key') == $regID){	
		$file->parentNode->removeChild($file);
		$msg='il tuo nuovo nick è'.$user;
		$test = $dom->save("C:\Program Files\Apache Software Foundation\Tomcat 7.0\users.xml");
		break;
		}
}
$registatoin_ids=array($regID);
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
$file=fopen("keyAdded.txt","a");
$data=$user.' '.$regID."\n";
fwrite($file,$data);
fclose($file);
curl_close($ch);	
echo $result;
$xml_doc = new DomDocument;
$xml_doc->Load('C:\Program Files\Apache Software Foundation\Tomcat 7.0\users.xml');
$child2 = $xml_doc->getElementsByTagName('Devices')->item(0);
$newelement=$xml_doc->createElement('User');
$newattribute=$xml_doc->createAttribute('name');
$newattribute -> value =$user;
$newattribute1=$xml_doc->createAttribute('key');
$newattribute1 -> value =$regID;
$newattribute2=$xml_doc->createAttribute('active');
$newelement -> appendChild($newattribute);
$newelement -> appendChild($newattribute1);
$newelement -> appendChild($newattribute2);
$child2 -> appendChild($newelement);
$test = $xml_doc->save("C:\Program Files\Apache Software Foundation\Tomcat 7.0\users.xml");
echo "<B>New Attribute Created<B>"

?>
