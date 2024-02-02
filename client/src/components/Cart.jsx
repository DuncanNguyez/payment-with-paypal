import CartItem from "./CartItems";

export default function Cart({cart, setCartHandler}) {
    
    return (
        <div className="cart">
            {cart.cartItems.map((item,index)=><CartItem setCartHandler={setCartHandler} index={index} item={item} key={index}/>)}
            <div className="p-2 text-right font-bold">Total: {cart.total}</div>
        </div>
    )
}