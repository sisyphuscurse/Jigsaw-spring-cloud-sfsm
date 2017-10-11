package com.dmall.shipping.apis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dmall.shipping.model.Shipping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class ShippingController {

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private List<Shipping> shippings = null;

	public ShippingController() throws ParseException {
		this.shippings = Arrays.asList(
				new Shipping("g001", "烟台1号库房", "北京市东城区A123号", formatter.parse("2015-04-23")),
				new Shipping("g002", "江西1号库", "北京市海淀区西二旗32号", formatter.parse("2015-05-12")),
				new Shipping("g003", "北京北七家", "北京朝阳大悦城3楼231号", formatter.parse("2015-04-27")));
	}

	@GetMapping
	public List<Shipping> getCommentsByTaskId() {

		return shippings;
	}

	@RequestMapping(value = "/{goodsId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Shipping getProductbyId(@PathVariable("goodsId") final String goodsId) {
		Optional<Shipping> shipping = shippings.stream().filter(c -> Objects.equals(c.getGoodsId(), goodsId)).findAny();

		return shipping.isPresent() ? shipping.get() : null;
	}
}
