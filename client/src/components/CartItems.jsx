export default function CartItem({ item, setCartHandler, index }) {
  return (
    <div className="cartItem flex-row flex justify-between items-center bg-red-50">
      <div className=" basis-1/4 p-2">{item.product.title}</div>
      <div>{item.product.price}</div>
      <div>
        <button
          onClick={() => {
            setCartHandler(index, item.quantity + 1);
          }}
          className="bg-slate-500 m-4 px-4"
        >
          +
        </button>
      </div>
      <div className="text-center  ">{item.quantity}</div>
      <div>
        <button
          onClick={() =>
            setCartHandler(index, item.quantity > 0 ? item.quantity - 1 : 0)
          }
          className="bg-slate-500 m-4 px-4"
        >
          -
        </button>
      </div>
      <div className="px-1 font-bold">{item.product.price * item.quantity}</div>
    </div>
  );
}
