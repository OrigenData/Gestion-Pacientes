  --Plantilla de insercion Empleados

  INSERT INTO public."Empleado"(
    "empNombre", "empEdad", "empRFC", "empEmail", "empTelefono")
    VALUES ('Andrew M. Russo', '45', '1Z 908 337 93 748 371 7', 'ousley@gustr.com', '0255987215');

  INSERT INTO public."Empleado"(
    "empNombre", "empEdad", "empRFC", "empEmail", "empTelefono")
    VALUES ('Carlton S. Davis', '71', '1Z 908 337 93 3762 371 7', 'CarltonSDavis@superrito.com', '9998916913');

  INSERT INTO public."Empleado"(
    "empNombre", "empEdad", "empRFC", "empEmail", "empTelefono")
    VALUES ('Jeremy D. Henderson', '26', '1Z V50 113 51 8321 382 2', 'JeremyDHenderson@gustr.com ', '2763266307');

  INSERT INTO public."Empleado"(
    "empNombre", "empEdad", "empRFC", "empEmail", "empTelefono")
    VALUES ('Elizebeth J. Boese', '33', '1Z 589 V64 27 8926 937 9', 'ElizebethJBoese@superrito.com ', '5465570283');

  INSERT INTO public."Empleado"(
    "empNombre", "empEdad", "empRFC", "empEmail", "empTelefono")
    VALUES ('Richard N. Roach', '29', '1Z 127 443 07 5385 608 0', 'RichardNRoach@gustr.com', '7317886333');

  INSERT INTO public."Empleado"(
    "empNombre", "empEdad", "empRFC", "empEmail", "empTelefono")
    VALUES ('Anthony A. Thibodeaux', '45', '1Z 570 67E 90 7624 098 5', 'AnthonyAThibodeaux@gustr.com', '4407011643');

  INSERT INTO public."Empleado"(
    "empNombre", "empEdad", "empRFC", "empEmail", "empTelefono")
    VALUES ('Eva F. Shultis', '37', '1Z 635 2F2 14 9082 163 0', 'EvaFShultis@gustr.com', '4295709962');

--Plantilla de insercion Servicio

  INSERT INTO public."Servicio"(
   "servNombre", "servHoraInicio", "servHoraSalida", "servEmpleado")
   VALUES ('Comunicación', '09:00:00', '10:00:00', 3);

  INSERT INTO public."Servicio"(
    "servNombre", "servHoraInicio", "servHoraSalida", "servEmpleado")
  	VALUES ('Conductual', '10:00:00', '11:00:00', 2);

  INSERT INTO public."Servicio"(
	  "servNombre", "servHoraInicio", "servHoraSalida", "servEmpleado")
	   VALUES ('Cognitiva/Académica', '11:00:00', '12:00:00', 1);

  INSERT INTO public."Servicio"(
	 "servNombre", "servHoraInicio", "servHoraSalida", "servEmpleado")
	 VALUES ('Motriz', '12:00:00', '14:00:00', 4);

  INSERT INTO public."Servicio"(
	 "servNombre", "servHoraInicio", "servHoraSalida", "servEmpleado")
	 VALUES ('Adaptativa', '14:00:00', '16:00:00', 6);

  INSERT INTO public."Servicio"(
   "servNombre", "servHoraInicio", "servHoraSalida", "servEmpleado")
	 VALUES ('Social', '16:00:00', '17:00:00', 7);

  INSERT INTO public."Servicio"(
   "servNombre", "servHoraInicio", "servHoraSalida", "servEmpleado")
   VALUES ('Autoayuda', '17:00:00', '18:00:00', 5);
