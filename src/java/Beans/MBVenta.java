/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Beans;

import Dao.DaoProducto;
import Dao.DaoVenta;
import Dao.DaoVentaDetalle;
import Pojos.Tproducto;
import Pojos.Tventa;
import Pojos.Tventadetalle;
import Util.HibernateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Ricardo
 */
@Named(value = "mBVenta")
@ViewScoped
public class MBVenta implements Serializable{

    Session session;
    Transaction transaction;
    
    private Tproducto producto;
    private List<Tproducto> listProductos;
    private Tventa venta;
    private List<Tventadetalle> listaVentaDetalle;
    private String codigoBarras;
    /**
     * Creates a new instance of MBVenta
     */
    public MBVenta() {
        this.venta = new Tventa();
        this.listaVentaDetalle = new ArrayList<>();
    }
    
    public List<Tproducto> getAllProductos()
    {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            DaoProducto daoProducto = new DaoProducto();
            this.transaction = this.session.beginTransaction();
            this.listProductos = daoProducto.getAllProductos(this.session);
            this.transaction.commit();
            return this.listProductos;
        } catch (Exception e) {
            if(this.transaction != null)
                this.transaction.rollback();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",e.getMessage()));
            return null;
        }
        finally{
            if(this.session != null)
                session.close();
        }
    }
    
    public void agregarListaVentaDetalle (int idProducto)
    {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            DaoProducto daoProducto = new DaoProducto();
            this.transaction = this.session.beginTransaction();
            this.producto = daoProducto.getProductoPorId(this.session, idProducto);
            this.transaction.commit();
            this.listaVentaDetalle.add(new Tventadetalle(null, null, this.producto.getCodigoBarras(),
                    this.producto.getNombre(), this.producto.getPrecioVentaUnitario(), 0, new BigDecimal("0")));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto","Producto agregado"));
            RequestContext.getCurrentInstance().update("frmRealizarVentas:tablaListaProductosVenta");
            RequestContext.getCurrentInstance().update("frmRealizarVentas:mesajeGeneral");
        } catch (Exception e) {
             if(this.transaction != null)
                this.transaction.rollback();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",e.getMessage()));
        }
        finally{
            if(this.session != null)
                session.close();
        }
    }
    
    public void agregarListaVentaDetallePorCodigoBarras()
    {
        this.session = null;
        this.transaction = null;
        try {
            if(this.codigoBarras.equals(""))
                return;
            
            this.session = HibernateUtil.getSessionFactory().openSession();
            DaoProducto daoProducto = new DaoProducto();
            this.transaction = this.session.beginTransaction();
            this.producto = daoProducto.getProductoPorCB(this.session, this.codigoBarras);
            if(this.producto != null)
            {
                this.listaVentaDetalle.add(new Tventadetalle(null, null, this.producto.getCodigoBarras(), this.producto.getNombre(),
                        this.producto.getPrecioVentaUnitario(), 0, new BigDecimal(0)));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto","Producto agregado"));
            }
            else
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Còdigo de barras no existente","Producto no encontrado"));
            }
            codigoBarras = "";
            this.transaction.commit();
            RequestContext.getCurrentInstance().update("frmRealizarVentas:tablaListaProductosVenta");
            RequestContext.getCurrentInstance().update("frmRealizarVentas:mesajeGeneral");
            RequestContext.getCurrentInstance().update("frmRealizarVentas:txtAgregarPorCodigoBarras");
        } catch (Exception e) {
             if(this.transaction != null)
                this.transaction.rollback();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",e.getMessage()));
        }
        finally
        {
            if(this.session != null)
                session.close();
        }
    }
    
    public void retirarListaVentaDetalle(String codigoBarras)
    {
        try {
            int i =0;
            for(Tventadetalle item : this.listaVentaDetalle)
            {
                if(item.getCodigoBarrasProducto().equals(codigoBarras))
                {
                    this.listaVentaDetalle.remove(i);
                    break;
                }
                i++;
            }
            BigDecimal totalVenta = new BigDecimal("0");
            for(Tventadetalle item : this.listaVentaDetalle)
            {
                BigDecimal totalVentaPorProducto = item.getPrecioVentaUnitarioProducto().multiply(new BigDecimal(item.getCantidad()));
                item.setTotalPrecioVenta(totalVentaPorProducto);
                totalVenta = totalVenta.add(totalVentaPorProducto);
            }
            this.venta.setPrecioVentaTotal(totalVenta);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Correcto","Producto retirado"));
            RequestContext.getCurrentInstance().update("frmRealizarVentas:tablaListaProductosVenta");
            RequestContext.getCurrentInstance().update("frmRealizarVentas:mesajeGeneral");
            RequestContext.getCurrentInstance().update("frmRealizarVentas:panelFinalVenta");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",e.getMessage()));
        }
    }
    
    public void calcularCostos()
    {
        try {
            BigDecimal totalVenta = new BigDecimal("0");
            for(Tventadetalle item : listaVentaDetalle)
            {
                BigDecimal totalVentaPorProducto = item.getPrecioVentaUnitarioProducto().multiply(new BigDecimal(item.getCantidad()));
                item.setTotalPrecioVenta(totalVentaPorProducto);
                totalVenta = totalVenta.add(totalVentaPorProducto);
            }
            this.venta.setPrecioVentaTotal(totalVenta);
            RequestContext.getCurrentInstance().update("frmRealizarVentas:tablaListaProductosVenta");
            RequestContext.getCurrentInstance().update("frmRealizarVentas:panelFinalVenta");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",e.getMessage()));
        }
    }
    
    public void realizarVenta()
    {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            DaoProducto daoProducto = new DaoProducto();
            DaoVenta daoVenta = new DaoVenta();
            DaoVentaDetalle daoVentaDetalle = new DaoVentaDetalle();
            this.transaction = this.session.beginTransaction();
            daoVenta.insertar(this.session, this.venta);
            this.venta = daoVenta.getUltimaVenta(this.session);
            
            for(Tventadetalle item : listaVentaDetalle)
            {
                this.producto = daoProducto.getProductoPorCB(this.session, item.getCodigoBarrasProducto());
                item.setTventa(this.venta);
                item.setTproducto(this.producto);
                daoVentaDetalle.insertar(this.session, item);
            }
            this.transaction.commit();
            this.listaVentaDetalle = new ArrayList<>();
            this.venta = new Tventa();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto","Venta realizada"));
        } catch (Exception e) {
              if(this.transaction != null)
                this.transaction.rollback();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",e.getMessage()));
        }
        finally{
             if(this.session != null)
                session.close();
        }
    }
}
