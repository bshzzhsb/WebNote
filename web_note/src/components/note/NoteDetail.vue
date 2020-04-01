<template>
	<div class="note-detail">
	<el-row>
		<el-col :span="bookmarkCol">
			<bookmark ref="bookmark"></bookmark>
		</el-col>

		<el-col :span="noteCol">
			<div>
				<el-card class="box-card note" v-model="note">
					<div slot="header" class="clearfix">
						<el-button style="float: left; padding: 3px 0" type="text" @click="goHistory">
							<i class="el-icon-back"></i>
						</el-button>
						<span>{{ note.name }}</span>
						<el-tooltip content="目录" placement="top">
							<el-button style="float: right; padding: 3px 0; margin-left: 10px" type="text" @click="bookmarkSwitch">
								<i class="el-icon-tickets"></i>
							</el-button>
						</el-tooltip>
						<el-tooltip content="编辑笔记" placement="top">
							<el-button style="float: right; padding: 3px 0" type="text" @click="editNote(note.id)">
								<i class="el-icon-edit"></i>
							</el-button>
						</el-tooltip>
					</div>
					<el-scrollbar style="height: 100%">
						<div v-html="note.contentHtml" class="note-html markdown-body" @click="quoteNote"></div>
					</el-scrollbar>
				</el-card>
			</div>
		</el-col>

		<el-col :span="quoteCol">
			<div style="text-align: initial">
				<el-card class="box-card note" v-model="note">
					<div slot="header" class="clearfix">
						<span>{{ quote.name }}</span>
						<el-button style="float: right; padding: 3px 0; margin-left: 10px" type="text" @click="closeQuote()">
							<i class="el-icon-close"></i>
						</el-button>
						<el-button style="float: right; padding: 3px 0" type="text" @click="editNote(note.id)">
							<i class="el-icon-edit"></i>
						</el-button>
					</div>
					<el-scrollbar style="height: 100%" id="quoteArea">
						<div v-html="quote.contentHtml" class="note-html markdown-body"></div>
					</el-scrollbar>
				</el-card>
			</div>
		</el-col>
	</el-row>
	</div>
</template>

<script>
    import Bookmark from "./Bookmark";
    import "github-markdown-css/github-markdown.css";
    import $ from "jquery"

    export default {
        name: "NoteDetail",
	    components: {Bookmark},
	    data () {
		    return {
			    note: {
			    },
			    quote: {
			    },
			    quoteLink: '',
			    bookmarkStatus: true,
			    quoteStatus: false,
			    bookmarkCol: 4,
			    noteCol: 20,
			    quoteCol: 0,
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
                this.axios.get('/note/' + id)
                    .then(function (response) {
                        if (response.data.status === 200) {
                            _this.note = response.data.object;
                            let newHtml = _this.appendPrefix(_this.note.contentHtml);
                            _this.note.contentHtml = _this.handleNoteLink(newHtml);
                            _this.getTitles(_this.note.contentHtml);
                        }
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },

	        appendPrefix(contentHtml) {
                let div = document.createElement("div");
                div.innerHTML = contentHtml;
                let doc = div.children;
                for (var i=0; i<doc.length; i++) {
                    if (doc[i].nodeName.indexOf("H") !== -1) {
                        let a = doc[i].children[0];
                        let originId = a.getAttribute("id");
                        a.setAttribute("id",'title_'+originId)
                    }
                }
                return div.innerHTML;
	        },

	        getTitles(contentHtml) {
                let div = document.createElement("div");
                div.innerHTML = contentHtml;
                let doc = div.children;
                let titles = [];
                for (var i=0; i<doc.length; i++) {
                    if (doc[i].nodeName.indexOf("H") !== -1) {
                        titles.push({
	                        href: "#"+doc[i].children[0].getAttribute("id"),
	                        name: doc[i].innerText,
	                        type: doc[i].nodeName[1],
                        })
                    }
                }
                this.$refs.bookmark.titles = titles;
	        },

            bookmarkSwitch() {
                if (this.bookmarkStatus) {
                    if (this.quoteStatus) {
                        this.colSwitch(0,12,12);
                    } else {
                        this.colSwitch(0,24,0);
                    }
                } else {
                    if (this.quoteStatus) {
                        this.colSwitch(4,10,10);
                    } else {
                        this.colSwitch(4,20,0);
                    }
                }
                this.bookmarkStatus = !this.bookmarkStatus;
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

            editNote(id) {
                this.$router.push({
                    path: '/note/edit',
                    name: 'NoteEdit',
                    query: {
                        noteId: id,
                    }
                })
            },

	        handleNoteLink(contentHtml) {
                let div = document.createElement("div");
                div.innerHTML = contentHtml;
                let doc = div.children;
                for (var i=0; i<doc.length; i++) {
                    if (doc[i].nodeName === "P") {
                        let as = doc[i].children;
                        for (var j=0; j<as.length;j++) {
                            if (as[j].nodeName === "A") {
	                            if (as[j].getAttribute("href").indexOf("notelink") !== -1) {
	                                as[j].removeAttribute("target");
                                    as[j].setAttribute("data-value", as[j].getAttribute("href"));
	                                as[j].setAttribute("href", "javascript:void(0)");
	                            }
                            }
                        }
                    }
                }
                return div.innerHTML;
	        },

            quoteNote(ev) {
                this.quoteLink = '';
                if (ev.target.nodeName === "A" && ev.target.getAttribute("data-value").indexOf("notelink") !== -1) {
	                var href = ev.target.getAttribute("data-value");
	                var noteId = href.match(/(\d+)[&]/)[1];
	                var titleId = href.match(/[&](.*)/)[1];
                    var _this = this;
                    if (this.quoteStatus === true && noteId === this.quote.id.toString()) {
                        this.quoteLink = titleId;
                    } else {
                        this.axios.get('/note/' + noteId)
                            .then(function (response) {
                                if (response.status === 200) {
                                    _this.quote = response.data.object;
                                    if (_this.bookmarkStatus) {
                                        _this.colSwitch(4,10,10);
                                    } else {
                                        _this.colSwitch(0,12,12);
                                    }
                                    _this.quoteStatus = true;
                                    _this.quoteLink = titleId;
                                }
                            })
                            .catch(function (error) {
                                console.log(error);
                            });
                    }
                }
            },

	        closeQuote() {
                if(this.bookmarkStatus) {
                    this.colSwitch(4,20,0);
                } else {
                    this.colSwitch(0,24,0);
                }
                this.quoteStatus = false;
                this.quote = [];
	        },

	        colSwitch(b,n,q) {
                this.bookmarkCol = b!=null?b:this.bookmarkCol;
                this.noteCol = n!=null?n:this.bookmarkCol;
                this.quoteCol = q!=null?q:this.bookmarkCol;
	        }
        },
	    watch: {
            quoteLink() {
                var _this = this;
                if (this.quoteLink !== '') {
                    this.$nextTick(function () {
	                    $('#quoteArea').children(":first").animate({
		                    scrollTop: document.getElementById(_this.quoteLink.toString()).offsetTop
	                    },300)
                    })
                }
            }
	    }
    }
</script>

<style>
	.note-detail .el-scrollbar__wrap{
		overflow-x: hidden;
	}
	.note-detail .el-card__body{
		height: 90%;
		box-sizing: border-box;
	}
	.note-detail .box-card{
		height: 100vh;
	}
	.note-detail .note {
		width: 95%;
		margin: auto;
	}
	.note-detail .note-html{
		height: 450px;
		text-align: left;
	}
</style>