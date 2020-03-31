<template>
	<div>
		<category-bar ref="categoryBar" @categorySelect="getNotes" @getCategory="getCategoryBar"></category-bar>
		<notes ref="notes" @editInfo="handleEditInfo" @addNote="handleAddNote" @updateInfo="getNotes" @editCategory="handleEditCategory"></notes>
		<note-edit-form ref="noteEditForm" @updateInfo="getNotes"></note-edit-form>
	</div>
</template>

<script>
	import Notes from "./Notes";
	import CategoryBar from "./CategoryBar";
	import NoteEditForm from "./NoteEditForm";

    export default {
        name: "Bookshelf",
	    components: {NoteEditForm, Notes, CategoryBar},
	    data() {
            return{

            }
	    },
	    methods: {
            getNotes() {
                var _this = this;
                var currentCid = this.$refs.categoryBar.currentCid;
                this.axios.get('/categories/'+currentCid+'/notes')
	                .then(function (response) {
	                    if (response.status === 200) {
                            _this.$refs.notes.notes = response.data;
                        }
                    })
            },
		    getCategoryBar() {
                var _this = this;
                this.axios.get("/categories")
                    .then(function (response) {
                        if (response.status === 200) {
                            _this.$refs.categoryBar.categories = response.data;
                        }
                    });
		    },
		    handleEditInfo(note) {
                this.$refs.noteEditForm.dialogFormVisible = true;
                this.$refs.noteEditForm.form = note;
                this.$refs.noteEditForm.isCreate = false;
            },
		    handleAddNote() {
                var currentCid = this.$refs.categoryBar.currentCid;
                this.$refs.noteEditForm.dialogFormVisible = true;
                this.$refs.noteEditForm.isCreate = true;
                this.$refs.noteEditForm.form = {};
                this.$refs.noteEditForm.cid = currentCid;
            },
		    handleEditCategory() {
                this.$refs.categoryBar.edit();
		    }
	    }
    }
</script>

<style scoped>

</style>