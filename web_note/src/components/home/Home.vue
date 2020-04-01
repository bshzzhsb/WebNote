<template>
	<div class="container">
	</div>
</template>

<script>
    export default {
        name: "Home"
    }
</script>

<style scoped>
	.container{
		z-index: -1;
		width: 100%;
		height: 100%;
		position: fixed;
		left: 0;
		top: 0;
		background-image: url("../../../static/home.png");
		background-size: contain;
		background-repeat: no-repeat;
		background-position: center;
	}
</style>