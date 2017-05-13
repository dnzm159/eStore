<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="container-wrapper">
	<div class="container">
		<div class="row">
			<div class="col-md-6">
				<img
					src="<c:url value="/resources/Images/${product.imageFilename}" />"
					alt="image" style="width: 80%" />
			</div>
			<div class="col-md-6">
				<h3>${product.name}</h3>
				<p>${product.description}</p>
				<p>
					<strong>Manufacturer</strong> : ${product.manufacturer}
				</p>
				…
			</div>
		</div>
	</div>
</div>
