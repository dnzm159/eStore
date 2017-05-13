<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%--스프링폼을 사용한다.  addProduct에서 객체를 넘겨주면 폼에잇는 자동적으로 맵핑이 이루어지므로--%>
<%--addProduct버튼을 누르게되면 get// submit버튼을 누르게되면 post--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="container-wrapper">
	<div class="container">

		<h1>Edit Product</h1>
		<p class="lead">Fill the below information to add product:</p>

		<%-- 버튼을 눌럿을때 action(url) --%>
		<sf:form
			action="${pageContext.request.contextPath}/admin/productInventory/editProduct?${_csrf.parameterName}=${_csrf.token}"
			method="post" modelAttribute="product" enctype="multipart/form-data">
			<%-- AdminContoller에서 product라는 이름으로 넘겨줫기때문 --%>


			<sf:hidden path="id" />
			<%-- 이부분이 없으면 edit를 할 때 id를 받는 부분이 없어서 id가 0이되버린다. --%>

			<div class="form-group">
				<label for="name">Name</label>
				<sf:input path="name" id="name" class="form-control" />
				<%--path의 Name은 Product의 name(필드이름) ->맵핑해준다 --%>
				<sf:errors path="name" cssStyle="color:#ff0000" />

			</div>

			<div class="form-group">
				<label for="category">Category: </label>
				<sf:radiobutton path="category" id="category" value="컴퓨터" />
				컴퓨터
				<sf:radiobutton path="category" id="category" value="가전" />
				가전
				<sf:radiobutton path="category" id="category " value="신발" />
				신발
			</div>


			<div class="form-group">
				<label for="description">Description</label>
				<sf:textarea path="description" id="description"
					class="form-control" />
			</div>

			<div class="form-group">
				<label for="price">Price</label>
				<sf:input path="price" id="price" class="form-control" />
				<sf:errors path="price" cssStyle="color:#ff0000" />
			</div>


			<div class="form-group">
				<label for="unitInStock">Unit In Stock</label>
				<sf:input path="unitInStock" id="unitInStock" class="form-control" />
				<sf:errors path="unitInStock" cssStyle="color:#ff0000" />
			</div>


			<div class="form-group">
				<label for="manufacturer">Manufacturer</label>
				<sf:input path="manufacturer" id="manufacturer" class="form-control" />
				<sf:errors path="manufacturer" cssStyle="color:#ff0000" />
			</div>


			<div class="form-group">
				<label for="productImage">Upload Picture</label>
				<sf:input path="productImage" id="productImage" type="file"
					class="form-control" />
			</div>
			<br>

			<input type="submit" value="submit" class="btn btn-default">
			<a href="<c:url value="/admin/productInventory" />"
				class="btn btn-default">Cancel</a>
		</sf:form>
		<br />
	</div>
</div>