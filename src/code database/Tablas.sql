

CREATE DATABASE "GestionPacientesBD"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

CREATE TABLE public."Empleado"
(
    "empID" serial NOT NULL,
    "empNombre" character varying(30),
    "empEdad" character varying(2),
    "empRFC" character varying(30),
    "empEmail" character varying(30),
    "empTelefono" character varying(10),
    CONSTRAINT "keyEmpID" PRIMARY KEY ("empID")
);


CREATE TABLE public."Servicio"
(
    "servID" serial NOT NULL,
    "servNombre" character varying(30),
    "servHoraInicio" time without time zone NOT NULL,
    "servHoraSalida" time without time zone NOT NULL,
    "servEmpleado" integer,
    CONSTRAINT "Servicio_pkey" PRIMARY KEY ("servID"),
    CONSTRAINT "Empleado_fkey" FOREIGN KEY ("servEmpleado")
        REFERENCES public."Empleado" ("empID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE public."Cita"
(
    "citaID" serial NOT NULL,
    --Cambiar formato de fecha
    "citaFecha" date,
    "citaServicio" integer,
    "citaPaciente" integer,
    CONSTRAINT "Cita_pkey" PRIMARY KEY ("citaID"),
    CONSTRAINT "Servicio_fkey" FOREIGN KEY ("citaServicio")
        REFERENCES public."Servicio" ("servID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
    CONSTRAINT "Paciente_fkey" FOREIGN KEY ("citaPaciente")
	REFERENCES public."Paciente" ("paciID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;
);

	

CREATE TABLE public."Paciente"
(
    "paciID" serial NOT NULL,
    "paciNombre" character varying(30),
    "paciEdad" character varying(2),
    "paciEmail" character varying(30),
    "paciTelefono" character varying(10),
    "paciCita" integer,
    CONSTRAINT "Paciente_pkey" PRIMARY KEY ("paciID"),
    CONSTRAINT "Cita_fkey" FOREIGN KEY ("paciCita")
        REFERENCES public."Cita" ("citaID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public."Familiar"
(
    "famID" serial NOT NULL,
    "famNombre" character varying(30),
    "famEdad" character varying(2),
    "famEmail" character varying(30),
    "famTelefono" character varying(10),
    "famPaciente" integer,
    CONSTRAINT "Familir_pkey" PRIMARY KEY ("famID"),
    CONSTRAINT "Paciente_fkey" FOREIGN KEY ("famPaciente")
        REFERENCES public."Paciente" ("paciID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
