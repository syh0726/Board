import { defineStore } from 'pinia'
import type { Profile } from "@/custom-types/profile";


export const useAuthStore=defineStore("auth",{
    state:() =>({
        Member:null as Profile | any,
    }),
    getters: {
        isLoggedIn:(state) =>{
            return state.Member!=null;
        },
        getProfile:(state)=>{
            return state.Member;
        },

    },
    actions: {
        login(member:Profile){
            this.Member=member;
        },
        logout(){
            this.Member=null;
        }
    },persist:{
        storage:sessionStorage,
    },
})