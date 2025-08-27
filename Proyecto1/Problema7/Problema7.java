//La Comisión Federal de Electricidad cobra el consumo de electricidad de acuerdo con un 
//tabulador basado en los kilowatts consumidos en el periodo. La tabla es la siguiente: 
//Costo del kW para Hogares: 
//De 0 a 250 kW el costo por kW es de $0.65 
//De 251 a 500 kW el costo por kW es de $0.85 
//De 501 a 1200 kW el costo por kW es de $1.50 
//De 1201 a 2100 kW el costo por kW es de $2.50 
//De 2101 kW hacia arriba el costo por kW es de $3.00 
//Costo del kW para Negocios:  
//$5.00, el costo es fijo por kilowatt sin importar la cantidad consumida. 
//Ejemplo: 
//a) Si en el hogar se consumen 737 kW durante el periodo entonces el total a pagar es de: 
//250*0.65 + 250*0,85 + 237* 1.50 = 162.50 + 212.50 + 355.50 = $730.50 
//b) Si en un negocio se consumen 250 kW, el valor a pagar serían de: 250* 5 = $1,250 
//Escribe un programa que pregunte la cantidad de kW consumidos, si el cliente tiene un contrato de 
//Hogar o de Negocio y dé como resultado la cantidad a pagar. 