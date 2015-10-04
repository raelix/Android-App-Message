<?php
$msg=$_GET['msg'];
$regID=$_GET['id'];
$registatoin_ids=array($regID);
$dom = new DomDocument;
$dom->load("C:\Program Files\Apache Software Foundation\Tomcat 7.0\users.xml" );
foreach( $dom->getElementsByTagName( 'User' ) as $file ) {
    //echo $file->getAttribute( 'name' );
    if($file->getAttribute('name') == $regID){
		//echo $file->getAttribute('key');
		$registatoin_ids=array($file->getAttribute('key'));
		break;
		}
}
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
$file=fopen("ip_log.txt","a");
$data=$regID.' '.$msg."\n";
fwrite($file,$data);
fclose($file);
curl_close($ch);
echo $result;
?>
