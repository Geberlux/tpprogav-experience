package bll;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JOptionPane;

import dll.DtoPedido;
import dll.DtoPedido_Producto;
import dll.DtoProducto;

public class Pedido {

    private int id;
    private int idpersona;
    
    public Pedido(int id, int idpersona) {
        super();
        this.id = id;
        this.idpersona = idpersona;
    }

    public Pedido(int idpersona) {
        super();
        this.idpersona = idpersona;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(int idpersona) {
        this.idpersona = idpersona;
    }

    @Override
    public String toString() {
        return "Pedido [id=" + id + ", idpersona=" + idpersona + "]";
    }
    
    public static void crearPedidoInteractivo(int idPersona) {
        Set<Integer> productosSeleccionados = new HashSet<>();
        StringBuilder resumen;
        while (true) {
            bll.Producto seleccionado = DtoProducto.buscarPorIDConFiltro();
            if (seleccionado == null) break;
            if (productosSeleccionados.contains(seleccionado.getId())) {
                JOptionPane.showMessageDialog(null, "Producto ya seleccionado.");
            } else {
                productosSeleccionados.add(seleccionado.getId());
            }
            int continuar = JOptionPane.showConfirmDialog(null, "¿Desea agregar otro producto?", "Continuar", JOptionPane.YES_NO_OPTION);
            if (continuar != JOptionPane.YES_OPTION) break;
        }
        if (productosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se seleccionaron productos. Pedido cancelado.");
            return;
        }
        resumen = new StringBuilder();
        for (Integer pid : productosSeleccionados) {
            bll.Producto p = DtoProducto.obtenerProductoPorID(pid);
            if (p != null) {
                resumen.append("ID:").append(p.getId()).append(" | ").append(p.getNombre()).append("\n");
            }
        }
        int confirmar = JOptionPane.showConfirmDialog(null, "Resumen del pedido:\n" + resumen.toString() + "\n¿Confirmar pedido?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Pedido no confirmado.");
            return;
        }
        int idPedido = DtoPedido.agregarPedido(new Pedido(idPersona));
        if (idPedido <= 0) {
            JOptionPane.showMessageDialog(null, "Error al crear pedido en la base de datos.");
            return;
        }
        boolean todoOk = true;
        for (Integer pid : productosSeleccionados) {
            bll.Pedido_producto detalle = new bll.Pedido_producto(idPedido, pid);
            boolean agregado = DtoPedido_Producto.agregarPedido_producto(detalle);
            if (!agregado) {
                todoOk = false;
            }
        }
        if (todoOk) {
            JOptionPane.showMessageDialog(null, "Pedido creado correctamente. ID pedido: " + idPedido);
        } else {
            JOptionPane.showMessageDialog(null, "Pedido creado pero hubo errores al agregar algunos detalles. Revise la base de datos.");
        }
    }
    
    public static void editarPedidoInteractivo(int idPersona) {
        LinkedList<Pedido> pedidos = DtoPedido.obtenerPedidosPorUsuario(idPersona);
        if (pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tiene pedidos.");
            return;
        }
        String[] opcionesPedidos = new String[pedidos.size()];
        for (int i = 0; i < pedidos.size(); i++) {
            opcionesPedidos[i] = "Pedido ID: " + pedidos.get(i).getId();
        }
        String elegido = (String) JOptionPane.showInputDialog(null, "Elija pedido", "", JOptionPane.QUESTION_MESSAGE, null, opcionesPedidos, opcionesPedidos[0]);
        if (elegido == null) return;
        int idPedido = Integer.parseInt(elegido.replaceAll("\\D+", ""));
        String[] acciones = {"Agregar producto", "Quitar producto", "Finalizar"};
        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(null, "Seleccione acción", "", 0, 0, null, acciones, acciones[0]);
            switch (opcion) {
                case 0:
                    bll.Producto seleccionado = DtoProducto.buscarPorIDConFiltro();
                    if (seleccionado != null) {
                        bll.Pedido_producto detalle = new bll.Pedido_producto(idPedido, seleccionado.getId());
                        boolean agregado = DtoPedido_Producto.agregarPedido_producto(detalle);
                        JOptionPane.showMessageDialog(null, agregado ? "Producto agregado al pedido" : "No se pudo agregar producto");
                    }
                    break;
                case 1:
                    LinkedList<String> detalles = DtoPedido_Producto.obtenerDetallesConProducto(idPedido);
                    if (detalles.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El pedido no tiene detalles.");
                        break;
                    }
                    String elegidoDetalle = (String) JOptionPane.showInputDialog(null, "Elija detalle a quitar", "", JOptionPane.QUESTION_MESSAGE, null, detalles.toArray(), detalles.get(0));
                    if (elegidoDetalle == null) break;
                    String[] partes = elegidoDetalle.split("\\|");
                    int idDetalle = -1;
                    for (String parte : partes) {
                        parte = parte.trim();
                        if (parte.startsWith("DetalleID:")) {
                            idDetalle = Integer.parseInt(parte.replace("DetalleID:", "").trim());
                            break;
                        }
                    }
                    if (idDetalle != -1) {
                        boolean eliminado = DtoPedido_Producto.eliminarDetalle(idDetalle);
                        JOptionPane.showMessageDialog(null, eliminado ? "Detalle eliminado" : "No se pudo eliminar detalle");
                    }
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        } while (opcion != 2 && opcion != JOptionPane.CLOSED_OPTION);
    }
    
    public static void eliminarPedidoInteractivo(int idPersona) {
        LinkedList<Pedido> pedidos = DtoPedido.obtenerPedidosPorUsuario(idPersona);
        if (pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tiene pedidos.");
            return;
        }
        String[] opcionesPedidos = new String[pedidos.size()];
        for (int i = 0; i < pedidos.size(); i++) {
            opcionesPedidos[i] = "Pedido ID: " + pedidos.get(i).getId();
        }
        String elegido = (String) JOptionPane.showInputDialog(null, "Elija pedido a eliminar", "", JOptionPane.QUESTION_MESSAGE, null, opcionesPedidos, opcionesPedidos[0]);
        if (elegido == null) return;
        int idPedido = Integer.parseInt(elegido.replaceAll("\\D+", ""));
        LinkedList<String> detalles = DtoPedido_Producto.obtenerDetallesConProducto(idPedido);
        StringBuilder sb = new StringBuilder();
        if (detalles.isEmpty()) {
            sb.append("El pedido no tiene productos.\n");
        } else {
            for (String d : detalles) sb.append(d).append("\n");
        }
        int confirmar = JOptionPane.showConfirmDialog(null, "Detalle del pedido:\n" + sb.toString() + "\n¿Eliminar pedido?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) return;
        boolean detallesEliminados = DtoPedido_Producto.eliminarPorPedido(idPedido);
        boolean pedidoEliminado = DtoPedido.eliminarPedido(idPedido);
        if (pedidoEliminado) {
            JOptionPane.showMessageDialog(null, "Pedido eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar pedido.");
        }
    }
    
    public static void mostrarPedidosUsuario(int idPersona) {
        LinkedList<Pedido> pedidos = DtoPedido.obtenerPedidosPorUsuario(idPersona);
        if (pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tiene pedidos.");
            return;
        }
        StringBuilder salida = new StringBuilder();
        for (Pedido p : pedidos) {
            salida.append("Pedido ID: ").append(p.getId()).append("\n");
            LinkedList<String> detalles = DtoPedido_Producto.obtenerDetallesConProducto(p.getId());
            if (detalles.isEmpty()) {
                salida.append("  (sin productos)\n");
            } else {
                for (String d : detalles) {
                    salida.append("  ").append(d).append("\n");
                }
            }
            salida.append("\n");
        }
        JOptionPane.showMessageDialog(null, salida.toString());
    }
    
}
