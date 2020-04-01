<template>
	<div class="category-bar">
		<el-tabs v-model="currentCid" type="card" @tab-click="handleClick" addable @edit="editCategory">
			<el-tab-pane
					v-for="(item, i) in categories"
					:key="item.id.toString()"
					:label="item.name"
					:name="item.id.toString()">
				{{item.content}}
			</el-tab-pane>
		</el-tabs>
		<category-edit-form ref="editForm" @getCategory="getCategoryBar"></category-edit-form>
	</div>
</template>

<script>
	import CategoryEditForm from "./CategoryEditForm";
    export default {
        name: "CategoryBar",
        components: {CategoryEditForm},
        data() {
            return {
                currentCid: "-1",
                categories: []
            }
        },
        mounted() {
            this.getCategoryBar();
        },
        methods: {
            getCategoryBar() {
                var _this = this;
                this.axios.get("/categories")
                    .then(function (response) {
                        if (response.status === 200){
                            _this.categories = response.data;
                            if (_this.categories.length > 0){
                                if (_this.$route.params.currentCid){
                                    _this.currentCid = _this.$route.params.currentCid.toString();
                                    console.log("goHistory");
                                } else {
                                    _this.currentCid = _this.categories[0].id.toString();
                                }
                                _this.$emit('categorySelect');
                            }
                        }
                    })
            },

            handleClick(tab, event) {
                this.$emit('categorySelect')
            },

	        editCategory(targetName, action) {
                if (action === 'add') {
	                this.$refs.editForm.dialogFormVisible = true;
	                this.$refs.editForm.isCreate = true;
	                this.$refs.editForm.formFlag = "新增笔记本分类";
	                this.$refs.editForm.form = {};
                }
	        },

	        edit() {
                for (var i=0; i < this.categories.length; i++) {
                    if (this.categories[i].id.toString() === this.currentCid) {
                        this.$refs.editForm.form = this.categories[i];
                    }
                }
                this.$refs.editForm.dialogFormVisible = true;
                this.$refs.editForm.isCreate = false;
                this.$refs.editForm.formFlag = "编辑笔记本分类";
	        },

            deleteCategory() {
                var _this = this;
                this.$confirm('此操作将永久删除该笔记分类, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var targetName = _this.currentCid;
                    var url = '/categories/delete/' + targetName;
                    console.log(url);
                    _this.axios.get(url)
                        .then(function (response) {
                            if (response.status === 200) {
                                _this.$message({
                                    type: 'success',
                                    message: '删除成功!'
                                });
                            }
                            let tabs = _this.categories;
                            let activeName = _this.currentCid;
                            if (activeName === targetName) {
                                tabs.forEach((tab, index) => {
                                    if (tab.id.toString() === targetName) {
                                        let nextTab = tabs[index+1] || tabs[index-1];
                                        if (nextTab) {
                                            activeName = nextTab.id.toString();
                                        }
                                    }
                                });
                            }
                            _this.currentCid = activeName;
                            _this.$emit('getCategory');
                            _this.$emit('categorySelect');
                        })
                        .catch(function (error) {
                            console.log(error)
                        });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
        },
    }
</script>

<style scoped>
	.category-bar{
		margin-top: 1em;
	}
</style>