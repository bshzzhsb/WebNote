<template>
	<div>
		<el-dialog title="编辑笔记本标题、简介"
		           :visible.sync="dialogFormVisible"
		           :before-close="handleClose">
			<el-form :model="form">
				<el-form-item label="笔记名称" :label-width="formLabelWidth">
					<el-input v-model="form.name" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="笔记简介" :label-width="formLabelWidth">
					<el-input type="textarea" :rows="3" v-model="form.abs" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible=false">取 消</el-button>
				<el-button type="primary" @click="updateNote">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
    export default {
        name: "NoteEditForm",
	    data() {
            return {
                isCreate: false,
                dialogFormVisible: false,
	            formLabelWidth: '120px',
	            form: {
	            },
	            cid: '',
            }
	    },
	    methods: {
            handleClose(done) {
                this.$emit('updateInfo');
                console.log('updateInfo');
                done();
            },
            updateNote() {
                var _this = this;
                var url;
                if (!_this.isCreate){
                    url = '/update/info/note/'+this.form.id;
                } else {
	                url = '/update/categories/'+this.cid.toString()+'/note/add';
                }
                this.axios.post(url, this.form)
                    .then(function (response) {
                        if (response.data.status === 200) {
                            _this.dialogFormVisible = false;
                            _this.$message({
                                message: '更新成功',
                                type: 'success'
                            });
                        } else {
                            _this.$message.error('更新失败');
                        }
                        _this.$emit('updateInfo');
                    })
                    .catch(function (error) {
                        _this.$message.error('更新失败');
                    })
            },
	    }
    }
</script>

<style scoped>

</style>