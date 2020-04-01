<template>
	<div>
		<el-menu :default-active="$route.path" class="" mode="horizontal" router>
			<el-menu-item v-for="(item,i) in navList" :index="item.url" :key="i">
				{{item.name}}
			</el-menu-item>

			<el-submenu index="2" style="float: right;">
				<template slot="title">{{ userFlag.name }}</template>
				<el-menu-item v-for="(item,i) in userFlag.menuList" :index="item.url" :key="i">{{ item.name }}</el-menu-item>
				<el-menu-item :style="{display: isLogin}" @click="logout">注销</el-menu-item>
			</el-submenu>
		</el-menu>
	</div>
</template>

<script>
    export default {
        name: "NavMenu",
        data() {
            return {
                isLogin: 'none',
                navList: [
	                {name: '首页', url: '/home'},
                    {name: '书架', url: '/bookshelf'},
                ],
	            userFlag: {
                    name: '',
                    url: '',
		            menuList: [
			            {url: '/home', name: '主页'},
		            ]
	            },
            };
        },
	    mounted() {
            if (window.localStorage.getItem("user") != null) {
                this.userFlag.name = JSON.parse(window.localStorage.getItem("user")).username;
                this.userFlag.menuList = [
	                {url: '/home', name: '用户中心'},
	                {url: '/bookshelf', name: '书架'},
                ];
	            this.isLogin = 'inline-block';
            } else {
                this.userFlag.name = "未登录";
                this.userFlag.menuList = [
	                {url: '/login', name: '登录'},
                ];
	            this.isLogin = 'none';
            }
	    },
        methods: {
	        logout() {
		        var _this = this;
		        this.axios.get('/logout')
			        .then(function (response) {
				        if (response.data.status === 200) {
				            _this.$store.commit('logout');
				            _this.$router.replace('/login');
				        }
                    })
			        .catch(function (error) {
				        console.log(error);
                    })
	        }
        }
    }
</script>

<style scoped>
	.el-menu-item{
		height: 40px;
		line-height: 40px;
	}
	.el-submenu /deep/ .el-submenu__title{
		height: 40px;
		line-height: 40px;
	}
</style>