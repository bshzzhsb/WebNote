<template>
	<div class="note-edit">
		<el-card class="box-card note" v-model="note" style="width: 100%">
			<div slot="header" class="clearfix">
				<el-button style="float: left; padding: 3px 0" type="text" @click="goHistory">
					<i class="el-icon-back"></i>
				</el-button>
				<span>{{ note.name }}</span>
				<el-button style="float: right; padding: 3px 0" type="text" @click="readNote">
					<i class="el-icon-more"></i>
				</el-button>
			</div>
			<mavon-editor v-model="note.contentMd" @save="saveNote" style="height: 100vh">
				<button slot="left-toolbar-after" class="op-icon" :title="`笔记链接`" @click="getCategoryList">
					<i class="el-icon-paperclip"></i>
				</button>
			</mavon-editor>
		</el-card>

		<el-dialog
				:visible.sync="dialogFormVisible"
				width="400px"
				center
				:title="'设置笔记链接'">
			<el-row>
				<el-form>
					<el-form-item >
						<el-select v-model="categoryId" placeholder="请选择链接笔记分类" @change="getNoteList" style="width: 100%" value="">
							<el-option v-for="(item,i) in categoryList" :label="item.name" :value="item.id" :key="item.id" ></el-option>
						</el-select>
					</el-form-item>
					<el-form-item v-model="noteLink" >
						<el-select v-model="noteLink.noteIndex" placeholder="请选择链接笔记" @change="getTitleList" style="width: 100%" value="">
							<el-option v-for="(item,i) in noteList" :label="item.name" :value="i" :key="item.id" ></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-select v-model="noteLink.titleID" placeholder="请选择链接标题" @change="generateLink" style="width: 100%" value="">
							<el-option v-for="item in titleList" :label="item.name" :value="item.id" :key="item.id"></el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</el-row>
			<el-row>
				<el-input style="width: 65%" v-model="noteLink.link" id="notelink"></el-input>
				<el-button
						data-clipboard-target="#notelink"
						data-clipboard-action="copy"
						type="primary"
						icon="el-icon-document-copy"
						style="font-size: 13px;margin-left: 0;width: 30%;float: right"
						class="copy-btn"
						@click="copyLink">
					复制
				</el-button>
			</el-row>
		</el-dialog>
	</div>
</template>

<script>
	import Clipboard from "clipboard"

    export default {
        name: "NoteEdit",
	    data() {
            return {
                categoryId: '',
                noteLink: {
                    noteIndex: '',
	                titleID: '',
	                link: '',
                },
	            dialogFormVisible: false,
	            categoryList: {},
	            noteList: {},
	            titleList: {},
                note: {
	                contentMd: '',
                }
            }
	    },
	    mounted() {
            if (this.$route.query.noteId) {
	            this.loadNote(this.$route.query.noteId);
            }
        },
	    methods: {
            loadNote(id) {
                var _this = this;
	            this.axios.get('/note/'+id)
		            .then(function (response) {
		                if (response.data.status === 200) {
		                    _this.note = response.data.object;
		                }
                    })
		            .catch(function(error) {
		                console.log(error);
		            })
            },

		    saveNote(value, render) {
                var url = '/update/content/note/'+this.note.id;
                var _this = this;
                this.note.contentMd = value;
                this.note.contentHtml = render;
                this.axios.post(url, this.note)
	                .then(function (response) {
		                if (response.status === 200) {
                            _this.$message({
                                type: 'success',
                                message: '保存成功!'
                            });
		                }
                    })
	                .catch(function (error) {
		                console.log(error)
                    })
		    },

            goHistory() {
	            console.log(this.note.category.id);
	            var _this = this;
	            this.$router.push({
		            path: '/bookshelf',
		            name: 'Bookshelf',
		            params: {
		                currentCid: this.note.category.id,
                    },
	            })
            },

		    readNote() {
			    this.$router.push({
                    path: '/note/detail',
                    name: 'NoteDetail',
                    query: {
                        noteId: this.note.id,
                    }
			    })
		    },

		    getCategoryList() {
                var _this = this;
                this.axios.get('/categories')
	                .then(function (response) {
		                if (response.status === 200) {
		                    _this.categoryList = response.data;
		                    _this.dialogFormVisible = true;
		                }
                    })
	                .catch(function (error) {
		                console.log(error);
                    })
		    },

		    getNoteList() {
                this.noteList = [];
                this.noteLink.noteIndex = '';
                this.noteLink.titleID = '';
                this.noteLink.link = '';
                var _this = this;
                var url = '/categories/'+this.categoryId+'/notes';
                _this.axios.get(url)
	                .then(function (response) {
		                if (response.status === 200) {
			                _this.noteList = response.data;
		                }
                    })
	                .catch(function (error) {
		                console.log(error);
                    })
		    },

		    getTitleList() {
                console.log(this.noteLink.noteIndex);
                this.titleList = [];
                this.noteLink.titleID = '';
                this.noteLink.link = '';
                var content = this.noteList[this.noteLink.noteIndex].contentHtml;
                let div = document.createElement("div");
                div.innerHTML = content;
                let doc = div.children;
                for (var i=0; i<doc.length; i++) {
                    if (doc[i].nodeName.indexOf("H") !== -1) {
                        this.titleList.push({
                            id: doc[i].children[0].getAttribute("id"),
                            name: doc[i].innerText,
                        })
                    }
                }
		    },

		    generateLink() {
			    this.noteLink.link = '';
			    var prefix = "notelink://";
			    this.noteLink.link = prefix + (this.noteList[this.noteLink.noteIndex].id)+"&"+this.noteLink.titleID;
		    },

		    copyLink() {
                let clipboard = new Clipboard('.copy-btn');
                clipboard.on('success', e => {
                    this.$message({
	                    type: 'success',
	                    message: '复制成功'
                    });
                    this.dialogFormVisible = false;
                    clipboard.destroy();
                });
                clipboard.on('error', e => {
                    this.$message('复制失败');
                    this.dialogFormVisible = false;
                    setTimeout(() => {
                    }, 500);
                    clipboard.destroy();
                })
		    },
	    }
    }
</script>

<style scoped>
	.note-edit /deep/ .el-card__body{
		padding: 0 !important;
	}
</style>