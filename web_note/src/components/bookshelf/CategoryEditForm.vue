<template>
	<div>
		<el-dialog title="编辑笔记本分类" :visible.sync="dialogFormVisible">
			<el-form :model="form">
				<el-form-item label="分类名称" :label-width="formLabelWidth">
					<el-input v-model="form.name" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible=false">取 消</el-button>
				<el-button type="primary" @click="onSubmit">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
    export default {
        name: "CategoryEditForm",
	    data() {
            return {
	            isCreate: false,
	            dialogFormVisible: false,
	            formLabelWidth: '120px',
	            form: {
	                id: '',
		            name: ''
	            },
            }
	    },
	    methods: {
		    onSubmit() {
		        var _this = this;
		        if (this.isCreate) {
                    this.axios.post('/categories/add', this.form)
                        .then(function (response) {
                            if (response.status === 200) {
                                _this.dialogFormVisible = false;
                                _this.$message({
                                    type: 'success',
                                    message: '新增成功!'
                                });
                                _this.$emit('getCategory');
                            }
                        })
                        .catch(function (error) {
                            console.log(error);
                        })
                } else {
                    this.axios.post('/categories/update', this.form)
                        .then(function (response) {
                            if (response.status === 200) {
                                _this.dialogFormVisible = false;
                                _this.$message({
                                    type: 'success',
                                    message: '更新成功!'
                                });
                                _this.$emit('getCategory');
                            }
                        })
                        .catch(function (error) {
                            console.log(error);
                        })
		        }
		    }
	    }
    }
</script>

<style scoped>

</style>