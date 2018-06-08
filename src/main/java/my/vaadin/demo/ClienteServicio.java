package my.vaadin.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteServicio {

	private static ClienteServicio instance;
	private static final Logger LOGGER = Logger.getLogger(ClienteServicio.class.getName());

	private final HashMap<Long, Cliente> contacts = new HashMap<>();
	private long nextId = 0;
	
	private ClienteServicio() {
	}

	public static ClienteServicio getInstance() {
		if (instance == null) {
			instance = new ClienteServicio();
			instance.ensureTestData();
		}
		return instance;
	}

	
	public synchronized List<Cliente> findAll() {
		return findAll(null);
	}

	
	public synchronized List<Cliente> findAll(String stringFilter) {
		ArrayList<Cliente> arrayList = new ArrayList<>();
		for (Cliente contact : contacts.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(ClienteServicio.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Cliente>() {

			@Override
			public int compare(Cliente o1, Cliente o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	
	public synchronized List<Cliente> findAll(String stringFilter, int start, int maxresults) {
		ArrayList<Cliente> arrayList = new ArrayList<>();
		for (Cliente contact : contacts.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(ClienteServicio.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Cliente>() {

			@Override
			public int compare(Cliente o1, Cliente o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		int end = start + maxresults;
		if (end > arrayList.size()) {
			end = arrayList.size();
		}
		return arrayList.subList(start, end);
	}

	public synchronized long count() {
		return contacts.size();
	}

	
	public synchronized void delete(Cliente value) {
		contacts.remove(value.getId());
	}

	
	public synchronized void save(Cliente entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE,
					"Customer is null. Are you sure you have connected your form to the application as described in tutorial chapter 7?");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Cliente) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		contacts.put(entry.getId(), entry);
	}

	/**
	 * ejemplo de los datos generado
	 */
	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] names = new String[] {  "Geovanny Mendoza", "Ketty Aguirre", "Valeria Ahumada",
					"Alexis Lopez", "Alejandro Duarte", "Andres pallares", "Vilmary Lopez", "Ulises Fritz","Ricardo Cantillo", "Matias Mendoza",
					"Sergio Jimenez", "Guillermo Bornacelly", "Empereatriz Villalba", "Julieth Rodriguez", "Claudia Mendoza", "Maria Mendoza" };
			Random r = new Random(0);
			for (String name : names) {
				String[] split = name.split(" ");
				Cliente c = new Cliente();
				c.setNombre(split[0]);
				c.setApellido(split[1]);
				c.setEmail(split[0].toLowerCase() + "@" + split[1].toLowerCase() + ".com");
				c.setUsuario(split[0].toLowerCase() + "." + split[1].toLowerCase());
				c.setEstado(ClienteEstado.values()[r.nextInt(ClienteEstado.values().length)]);
                int daysOld = 0 - r.nextInt(365 * 15 + 365 * 60);
                c.setFechanacimiento(LocalDate.now().plusDays(daysOld));
				save(c);
			}
		}
	}
}
