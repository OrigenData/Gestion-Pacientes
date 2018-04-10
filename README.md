GtkTreeViewSQL
=========================
**Description:** Captura nombre y edad de usuarios y los almacena en una base de datos PostgreSQL

**License:** GNU General Public License v3.0

**Source code:** https://github.com/OrigenData/GestionPacientes/

**Documentation:** https://www.origendata.com/

###### Screenshot
![Screenshot](https://origendata.files.wordpress.com/2018/04/gtktreeviewsql.png)



### Dependencies

**Gestion Pacientes** requiere que se instalen las siguientes librerias para ejecutar:
* GTK 3.20
* java-gnome 4.1.3
* PostgreSQL JDBC 4.2

### Tabla del la Base de Datos
La base de datos se llama TreeView y se crea una tabla nombrada "Persona"

```
CREATE TABLE public."Persona"
(
    "perID" serial NOT NULL,
    "perNombre" character varying(30),
    "perEdad" character varying(2),
    CONSTRAINT "Persona_pkey" PRIMARY KEY ("perID")
)
```