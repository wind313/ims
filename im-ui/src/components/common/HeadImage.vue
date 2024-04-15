<template>
	<div class="head-image" @click="showUserInfo($event)">
		<img :src="url" :style="{width: size+'px',height: size+'px',cursor: 'pointer'}" />
		<slot></slot>
	</div>
</template>

<script>

	export default {
		name: "headImage",
		data() {
			return {}
		},
		props: {
			id:{
				type: Number
			},
			size: {
				type: Number,
				default: 50
			},
			url: {
				type: String
			}
		},
		methods:{
			showUserInfo(e){
				if(this.id && this.id>0){
					this.$http({
						url: `/user/find/${this.id}`,
						method: 'get'
					}).then((user) => {
						this.$store.commit("setUserInfoBoxPos",e);
						this.$store.commit("showUserInfoBox",user);
					})
				}
			}
		}
	}
</script>

<style scoped lang="scss">
	.head-image {
		border-radius: 50% !important;
		position: relative;
		img {
			position: relative;
			overflow: hidden;
			border-radius: 50%;
		}

		img:before {
			content: '';
			display: block;
			border-radius: 50%;
			width: 100%;
			height: 100%;
			background: url('../../assets/default_head.png') no-repeat 0 0;
			background-size: 100%;
		}
	}
</style>
