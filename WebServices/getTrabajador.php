<?php /*  * El siguiente código localiza un alumno  * AGZ    3/11/2016  */ 
 
$response = array(); 
 
$Cn = mysqli_connect("localhost","root","","tsocial")or die ("server no encontrado"); 
mysqli_set_charset($Cn,"utf8"); 
 
// Checa que le este llegando por el método GET el nocontrol 
if ($_SERVER['REQUEST_METHOD']=='POST') {  
    $obj= json_decode(file_get_contents("php://input"),true);

    $correo = $obj['correo']; 
    $result = mysqli_query($Cn,"SELECT idtrabajador,nombre,correo,domicilio,telefono,password from trabajadores WHERE correo = '$correo'"); 
 
    if (!empty($result)) {         
        if (mysqli_num_rows($result) > 0) { 
 
            $result = mysqli_fetch_array($result);             
            $trabajador = array(); 
 
            $trabajador["idtrabajador"] = $result["idtrabajador"];       
            $trabajador["nombre"] = $result["nombre"]; 
            $trabajador["correo"] = $result["correo"]; 
            $trabajador["domicilio"] = $result["domicilio"]; 
            $trabajador["telefono"] = $result["telefono"];
            $trabajador["password"] = $result["password"]; 

            $response["success"] = 200;   
            $response["message"] = 'trabajador encontrado';   
            $response["trabajadores"] = array(); 
 
           array_push($response["trabajadores"], $trabajador); 
 
           // codifica la información en formato de JSON response            
           echo json_encode($response);         } 
           else {             
            // No Encontro al alumno             
            $response["success"] = 404;  
            //No encontro información y el success = 0 indica no exitoso             
            $response["message"] = "Trabajador no encontrado";             
            echo json_encode($response);         }     } 
            else {         
                $response["success"] = 404;  //No encontro información y el success = 0 indica no exitoso         
                $response["message"] = "Trabajador no encontrado no encontrado";         
                echo json_encode($response);     } } 
                else {     
                    // required field is missing     
                    $response["success"] = 400;     
                    $response["message"] = "Faltan Datos de entrada"; 
    
                    echo json_encode($response); 
}
mysqli_close($Cn); 
?> 