package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValoresIguaisGroup {
	private long quantity;
	private String id;
	private List<ValoresIguaisItem> items;

	public ValoresIguaisGroup(String keyname, Map<String, Long> results, long refNumber) {
		this.id = keyname;
		this.quantity = refNumber;
		this.items = new ArrayList<ValoresIguaisItem>();
		this.items.addAll(this.parseFrom(results));
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ValoresIguaisItem> getItems() {
		return items;
	}

	public void setItems(List<ValoresIguaisItem> items) {
		this.items = items;
	}

	private List<ValoresIguaisItem> parseFrom(Map<String, Long> results) {
		return results.entrySet().stream().map(e -> {
			ValoresIguaisItem v = new ValoresIguaisItem();
			v.setIndex(e.getKey());
			v.setValue(e.getValue());
			v.setDispersion(BigDecimal.valueOf(e.getValue()).divide(BigDecimal.valueOf(quantity), 5, BigDecimal.ROUND_HALF_UP));
			return v;
		}).collect(Collectors.toList());

	}

	@Override
	public String toString() {
		return "ValoresIguaisGroup [quantity=" + quantity + ", id=" + id + ", items=" + items + "]";
	}

}
