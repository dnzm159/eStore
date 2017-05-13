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
 �̹��� ��Ϲ��
 1. CommonsMultipartResolver �� ���
 2. 1�� commons.fileupload.jar�� ����ϱ� ������ jar �ٿ�
 3. form�� enctype="multipart/form-data"�߰� ����
 4. inputType���� file�� �ش�.
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
		product.setCategory("��ǻ��");
		model.addAttribute("product", product);
		return "addProduct";
	}

	@RequestMapping(value = "/productInventory/addProduct", method = RequestMethod.POST) // ����// URL������// Post�ϰ��
	public String addProductPost(@Valid Product product, BindingResult result, HttpServletRequest request) { // ����ڰ�
																												// �Է���
																												// form�����Ͱ�
																												// ���ε���
																												// �̷������.(����)
		// product�� result�� ���� �ڵ����� insert�Ǿ view�� ���ư���. ->view���� product��
		// result�� �� �� ����
		// �߸��� �Է������� ����ڰ� �Է��� ������ ȭ�鿡 �ٽ� ��Ÿ����.
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
		// return "productInventory"; //������ �մ� service��ü�� dao������ db�� ������ ���ٵ�
		// productInventory����ϴ� jsp�����̰��Եȴ�
		// product �� ������ ���޾ȵ� ->redirect�� ���Ѿ���

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

		Product product = productService.getProductById(id); // db��ȸ

		model.addAttribute("product", product); // �������Է��޴����� ��Ÿ���ߵǼ�

		return "editProduct";
	}

	@RequestMapping(value = "/productInventory/editProduct", method = RequestMethod.POST)
	public String editProductPost(@Valid Product product, BindingResult result,
			HttpServletRequest request) { // ������
																					// ��ü��
																					// ���ε��ڵ�(���ε�)

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
