package kr.ac.hansung.cse.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.service.ProductService;

/*
 이미지 등록방법
 1. CommonsMultipartResolver 빈 등록
 2. 1이 commons.fileupload.jar를 사용하기 때문에 jar 다운
 3. form에 enctype="multipart/form-data"추가 삽입
 4. inputType으로 file을 준다.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ProductService productService;

	@RequestMapping()
	public String adminPage() {
		return "admin";
	}

	@RequestMapping("/productInventory")
	public String getProduct(Model model) {
		List<Product> products = productService.getProducts();
		model.addAttribute("products", products);

		return "productInventory";
	}

	@RequestMapping("/productInventory/addProduct")
	public String addProduct(Model model) {
		Product product = new Product();
		product.setCategory("컴퓨터");
		model.addAttribute("product", product);
		return "addProduct";
	}

	@RequestMapping(value = "/productInventory/addProduct", method = RequestMethod.POST) // 같은// URL이지만// Post일경우
	public String addProductPost(@Valid Product product, BindingResult result, HttpServletRequest request) { // 사용자가
																												// 입력한
																												// form데이터가
																												// 바인딩이
																												// 이루어진다.(맵핑)
		// product와 result는 모델이 자동으로 insert되어서 view로 날아간다. ->view에서 product와
		// result를 볼 수 있음
		// 잘못된 입력이지만 사용자가 입력한 내용이 화면에 다시 나타난다.
		if (result.hasErrors()) {
			System.out.println("===Form data has some errors===");
			List<ObjectError> errors = result.getAllErrors();

			for (ObjectError error : errors) {
				System.out.println(error.getDefaultMessage());
			}
			return "addProduct";
		}
		
		MultipartFile productImage = product.getProductImage();
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path savePath = Paths.get(rootDirectory + "\\resources\\images\\" + productImage.getOriginalFilename());
		if (productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(savePath.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		product.setImageFilename(productImage.getOriginalFilename());
		if (productImage.isEmpty() == false) {
			System.out.println("------- file start ------");
			System.out.println("name: " + productImage.getName());
			System.out.println("filename: " + productImage.getOriginalFilename());
			System.out.println("filename: " + productImage.getSize());
			System.out.println("------- file end -------");
		}
		if (!productService.addProduct(product)) {
			System.out.println("Adding Product cannot be done");
		}
		// return "productInventory"; //문제가 잇다 service객체가 dao를통해 db에 저장을 할텐데
		// productInventory라고하는 jsp파일이가게된다
		// product 의 내용이 전달안됨 ->redirect를 시켜야함

		return "redirect:/admin/productInventory";
	}

	@RequestMapping("/productInventory/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id, HttpServletRequest request) {
 
		Product product = productService.getProductById(id);
		
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path path = Paths.get(rootDirectory + "\\resources\\images\\" + product.getImageFilename());
		
		if(Files.exists(path)){
			
			try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		if (!productService.deleteProductById(id)) {
			System.out.println("Deleting product cannot be done");
		}
		return "redirect:/admin/productInventory";
	}

	@RequestMapping("/productInventory/editProduct/{id}")
	public String editProduct(@PathVariable int id, Model model) {

		Product product = productService.getProductById(id); // db조회

		model.addAttribute("product", product); // 기존에입력햇던값이 나타나야되서

		return "editProduct";
	}

	@RequestMapping(value = "/productInventory/editProduct", method = RequestMethod.POST)
	public String editProductPost(@Valid Product product, BindingResult result,
			HttpServletRequest request) { // 수정한
																					// 객체가
																					// 맵핑됨자동(바인딩)

		if (result.hasErrors()) {
			System.out.println("===Form data has some errors===");
			List<ObjectError> errors = result.getAllErrors();

			for (ObjectError error : errors) {
				System.out.println(error.getDefaultMessage());
			}
			return "editProduct";
		}

		
		MultipartFile productImage = product.getProductImage();
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path savePath = Paths.get(rootDirectory + "\\resources\\images\\" + productImage.getOriginalFilename());
		if (productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(savePath.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		product.setImageFilename(productImage.getOriginalFilename());
		if (!productService.editProduct(product)) {
			System.out.println("Editing Product cannot be done");
		}
		System.out.println(product.getId());
		return "redirect:/admin/productInventory";
	}

}
