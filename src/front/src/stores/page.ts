import {defineStore} from "pinia";
export const usePageStore=defineStore("page",{
    state:() =>({
        maxPage:0,
        currentPage:0,
    }),
    getters: {
        getMaxPage:(state) =>{
            return state.maxPage;
        },
        getCurrentPage:(state)=>{
            return state.currentPage;
        }
    },
    actions: {
        setMaxPage(maxPage:any){
            this.maxPage=maxPage;
        },
        setCurrentPage(currentPage:any){
            this.currentPage=currentPage;
        }
    },persist:{
        storage:sessionStorage,
    },
})