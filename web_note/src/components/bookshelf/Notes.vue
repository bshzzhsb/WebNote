<template>
	<div style="text-align: left;">
		<el-row :gutter="20" style="margin: 0;">
		<el-col v-for="(item,i) in notes" :key="i" :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
		<el-card class="box-card" shadow="hover" style="width:100%; margin-bottom: 1em">
			<div slot="header" class="clearfix">
				<span>{{item.name}}</span>
				<el-tooltip content="删除此笔记" placement="top">
					<el-button style="float: right; padding: 3px 0; margin-left: 10px" type="text" @click="deleteNote(item.id)">
						<i class="el-icon-delete"></i>
					</el-button>
				</el-tooltip>
				<el-tooltip content="编辑笔记标题与简介" placement="top">
					<el-button style="float: right; padding: 3px 0" type="text" @click="editInfo(item)">
						<i class="el-icon-edit"></i>
					</el-button>
				</el-tooltip>
				<el-tooltip class="item" content="查看笔记内容" placement="top">
					<el-button style="float: right; padding: 3px 0" type="text" @click="readNote(item.id)">
						<i class="el-icon-more"></i>
					</el-button>
				</el-tooltip>
				<el-tooltip content="编辑笔记内容" placement="top">
					<el-button style="float: right; padding: 3px 0" type="text" @click="editNote(item.id)">
						<i class="el-icon-edit-outline"></i>
					</el-button>
				</el-tooltip>
			</div>
			<div class="text item">
				{{restrict(item.abs)}}
			</div>
		</el-card>
		</el-col>

			<el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
			<el-card class="box-card" shadow="hover" style="width: 100%">
				<div slot="header" class="clearfix">
					<span>新增笔记本</span>
					<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
				</div>
				<div class="text item" style="text-align: center;" @click="addNote">
					<i class="el-icon-plus"></i>
				</div>
			</el-card>
			</el-col>
		</el-row>

		<el-row style="position: fixed;bottom: 60px; right: 10px">
			<el-button type="primary" icon="el-icon-edit" circle @click="editCategory"></el-button>
		</el-row>
		<el-row style="position: fixed;bottom: 10px; right: 10px">
			<el-button type="danger" icon="el-icon-delete" circle @click="deleteCategory"></el-button>
		</el-row>
	</div>
</template>

<script>
    export default {
        name: "Notes",
	    data() {
            return {
                notes: []
            }
	    },
	    methods: {
            restrict(abs) {
                if (abs.length < 24) {
                    return abs;
                } else {
                    return abs.substring(0,24) + '...';
                }
            },
            editInfo(note) {
                this.$emit('editInfo', note)
            },
		    addNote() {
                this.$emit('addNote')
		    },
            editNote(id) {
	            this.$router.push({
		            path: '/note/edit',
		            name: 'NoteEdit',
		            query: {
		                noteId: id,
		            }
	            })
            },
            readNote(id) {
                this.$router.push({
                    path: '/note/detail',
                    name: 'NoteDetail',
                    query: {
                        noteId: id,
                    }
                })
            },
		    deleteNote(id) {
                var _this = this;
                this.$confirm('此操作将永久删除该笔记本, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _this.axios.get('/note/delete/'+id)
                        .then(function (response) {
                            if (response.status === 200) {
                                _this.$message({
                                    type: 'success',
                                    message: '删除成功!'
                                });
                            }
                            _this.$emit('updateInfo');
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
		    editCategory() {
                this.$emit('editCategory');
		    },
            deleteCategory() {
                this.$emit('deleteCategory');
            },
	    }
    }
</script>

<style scoped>
	.el-card /deep/ .el-card__body{
		height: 50px;
	}
</style>