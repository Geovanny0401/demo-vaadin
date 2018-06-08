package my.vaadin.demo;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class ClienteForm extends FormLayout {
	
	private TextField nombre = new TextField("Nombre");
	private TextField apellido = new TextField("Apellido");
	private TextField email = new TextField("Email");
	private TextField usuario = new TextField("Usuario");
    private NativeSelect<ClienteEstado> estado = new NativeSelect<>("Estado");
    private DateField fechaNacimiento= new DateField("Fecha Nacimiento");
    private Button guardar = new Button("Guardar");
    private Button eliminar = new Button("Eliminar");

    private ClienteServicio servicio = ClienteServicio.getInstance();
    private Cliente cliente;
    private MyUI myUI;
    private Binder<Cliente> binder = new Binder<>(Cliente.class);
    
    
    public ClienteForm(MyUI myUI) {
    	this.myUI=myUI;
    	setSizeUndefined();
    	HorizontalLayout buttons = new HorizontalLayout(guardar, eliminar);
    	addComponents(nombre, apellido, email, usuario, estado, fechaNacimiento, buttons);
    	
    	estado.setItems(ClienteEstado.values());
    	guardar.setStyleName(ValoTheme.BUTTON_PRIMARY);
    	guardar.setClickShortcut(KeyCode.ENTER);
    	binder.bindInstanceFields(this);
    	
    	guardar.addClickListener(e-> this.guardar());
    	eliminar.addClickListener(e-> this.eliminar());
    	
    	
	}
      
    public void setCliente(Cliente cliente) {
		this.cliente = cliente;
		binder.setBean(cliente);
		eliminar.setVisible(cliente.isPersisted());
		setVisible(true);
		nombre.selectAll();
		
	}
    
    private void eliminar()
    {
    		servicio.delete(cliente);
    		myUI.ActualizarListado();
    		setVisible(false);
    }
    
    private void guardar()
    {
    	      servicio.save(cliente);
    	      myUI.ActualizarListado();
    	      setVisible(false);
    }
    
    
}
