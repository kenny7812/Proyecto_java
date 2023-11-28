import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class proveedor {

    private static final String URL = "jdbc:mysql://localhost:3306/Sistema_Inventario";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "100122";

    public static void main(String[] args) {
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Seleccione una operación:");
                System.out.println("1. Consulta Producto");
                System.out.println("2. Inserción Producto");
                System.out.println("3. Actualización Producto");
                System.out.println("4. Eliminación Producto");
                System.out.println("5. Consulta Proveedor");
                System.out.println("6. Inserción Proveedor");
                System.out.println("7. Actualización Proveedor");
                System.out.println("8. Eliminación Proveedor");
                System.out.println("0. Salir");

                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        consultarDatosProveedor(conexion);
                        break;
                    case 2:
                        insertarDatosProveedor(conexion);
                        break;
                    case 3:
                        actualizarDatosProveedor(conexion);
                        break;
                    case 4:
                        eliminarDatosProveedor(conexion);
                        break;
                    case 0:
                        System.out.println("Saliendo del programa.");
                        System.exit(0);
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Métodos CRUD para Producto (los que ya tenías)

    // Métodos CRUD para Proveedor
    private static void consultarDatosProveedor(Connection conexion) throws SQLException {
        String consultaSQL = "SELECT * FROM Proveedor";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                int idProveedor = resultado.getInt("ID_Proveedor");
                String nombreEmpresa = resultado.getString("Nombre_Empresa");
                String direccion = resultado.getString("Direccion");
                String telefono = resultado.getString("Telefono");

                System.out.println("ID Proveedor: " + idProveedor);
                System.out.println("Nombre de la Empresa: " + nombreEmpresa);
                System.out.println("Dirección: " + direccion);
                System.out.println("Teléfono: " + telefono);
                System.out.println("--------------------");
            }
        }
    }

    private static void insertarDatosProveedor(Connection conexion) throws SQLException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese los datos para la inserción del proveedor:");

            System.out.print("Nombre de la Empresa: ");
            String nombreEmpresa = scanner.nextLine();
            System.out.print("Dirección: ");
            String direccion = scanner.nextLine();
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();

            String consultaSQL = "INSERT INTO Proveedor (Nombre_Empresa, Direccion, Telefono) VALUES (?, ?, ?)";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, nombreEmpresa);
                consulta.setString(2, direccion);
                consulta.setString(3, telefono);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }
        }
    }

    private static void actualizarDatosProveedor(Connection conexion) throws SQLException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del proveedor a actualizar:");
            int idProveedor = scanner.nextInt();

            if (!existeProveedor(conexion, idProveedor)) {
                System.out.println("El proveedor con ID " + idProveedor + " no existe.");
                return;
            }

            System.out.println("Ingrese los nuevos datos para la actualización:");

            System.out.print("Nombre de la Empresa: ");
            String nombreEmpresa = scanner.next();
            System.out.print("Dirección: ");
            String direccion = scanner.next();
            System.out.print("Teléfono: ");
            String telefono = scanner.next();

            String consultaSQL = "UPDATE Proveedor SET Nombre_Empresa = ?, Direccion = ?, Telefono = ? WHERE ID_Proveedor = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setString(1, nombreEmpresa);
                consulta.setString(2, direccion);
                consulta.setString(3, telefono);
                consulta.setInt(4, idProveedor);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }
        }
    }

    private static void eliminarDatosProveedor(Connection conexion) throws SQLException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del proveedor a eliminar:");
            int idProveedor = scanner.nextInt();

            if (!existeProveedor(conexion, idProveedor)) {
                System.out.println("El proveedor con ID " + idProveedor + " no existe.");
                return;
            }

            String consultaSQL = "DELETE FROM Proveedor WHERE ID_Proveedor = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
                consulta.setInt(1, idProveedor);

                int filasAfectadas = consulta.executeUpdate();
                System.out.println(filasAfectadas + " fila(s) afectada(s).");
            }
        }
    }

    private static boolean existeProveedor(Connection conexion, int idProveedor) throws SQLException {
        String consultaSQL = "SELECT 1 FROM Proveedor WHERE ID_Proveedor = ?";
        try (PreparedStatement consulta = conexion.prepareStatement(consultaSQL)) {
            consulta.setInt(1, idProveedor);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next();
            }
        }
    }
}
