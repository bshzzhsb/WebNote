<template>
	<div>
		<el-menu :default-active="$route.path" class="el-menu-demo" mode="horizontal" router>
			<el-menu-item v-for="(item,i) in navList" :index="item.url" :key="i">
				{{item.name}}
			</el-menu-item>
		</el-menu>
	</div>
</template>

<script>
    export default {
        name: "NavMenu",
        data() {
            return {
                navList: [
	                {name: '首页', url: '/home'},
                    {name: '书架', url: '/bookshelf'},
                ],
            };
        },
        methods: {

        }
    }
</script>

<style scoped>

</style>