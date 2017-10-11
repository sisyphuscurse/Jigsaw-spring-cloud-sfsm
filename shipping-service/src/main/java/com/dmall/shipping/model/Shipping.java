package com.dmall.shipping.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.Date;

public class Shipping {

	private String goodsId;

	private String warehouse;

	private String address;

	private Date posted;

	public Shipping() {
		super();

	}

	public Shipping(String goodsId, String warehouse, String address, Date posted) {
		super();
		this.goodsId = goodsId;
		this.warehouse = warehouse;
		this.address = address;
		this.posted = posted;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	@JsonSerialize(using = CustomDateToLongSerializer.class)
	public Date getPosted() {
		return posted;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPosted(Date posted) {
		this.posted = posted;
	}

}

class CustomDateToLongSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
		throws IOException, JsonProcessingException {
		jgen.writeNumber(value.getTime());
	}
}


