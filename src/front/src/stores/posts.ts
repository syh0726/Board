import { defineStore } from 'pinia'
import type {Post} from "@/custom-types/post";

export const usePostsStore=defineStore("post",{
    state:() =>({
        posts:[] as Post[],
        postId:0,
        category:'FREE',
    }),
    getters: {
        getPostId:(state) =>{
            return state.postId;
        },
        getCategory:(state)=>{
            return state.category;
        },
        getPosts:(state)=>{
            return state.posts;
        }
    },
    actions: {
        setPostId(postId:any){
            this.postId=postId;
        },
        setCategory(category:any){
            this.category=category;
        },
        addPost(p:Post[]){
            this.posts.push(...p);
        },
        clean(){
            this.posts=[];
        }

    },persist:{
        storage:sessionStorage,
    },
})