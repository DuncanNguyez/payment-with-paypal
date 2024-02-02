import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";
import Cart from "./components/Cart";
import { useCallback, useState } from "react";
import transformVariableType from "./utils/transformVariableType";

export default function App() {
  const [cart, setCart] = useState({
    cartItems: [
      {
        product: { title: "product1", price: 12 },
        quantity: 2,
        subTotal: 12 * 2,
      },
      {
        product: { title: "product2", price: 15 },
        quantity: 1,
        subTotal: 15 * 1,
      },
      {
        product: { title: "product3", price: 19 },
        quantity: 4,
        subTotal: 19 * 4,
      },
    ],
    total: 12 * 2 + 15 + 19 * 4,
  });
  const setCartHandler = useCallback((index, quantity) => {
    setCart((prevCart) => {
      const newCart = { ...prevCart };
      newCart.cartItems[index].quantity = quantity;
      newCart.cartItems[index].subTotal =
        quantity * newCart.cartItems[index].product.price;
      newCart.total = newCart.cartItems.reduce(
        (total, it) => total + it.product.price * it.quantity,
        0
      );
      console.log(newCart);
      return newCart;
    });
  }, []);

  const createOrder = async () => {
    const payload = transformVariableType(cart, "sneck_case");
    console.log({ payload });
    const res = await fetch("http://localhost:8080/api/v1/orders", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });
    const jsonData = await res.json();
    const data = JSON.parse(jsonData.data);
    console.log({data});
    return data.id;
  };

  const onApprove = async (data) => {
    const res = await fetch(
      `http://localhost:8080/api/v1/orders/${data.orderID}/capture`,
      {
        method: "post",
      }
    );
    const jsonData = await res.json();
    console.log({ jsonData });
  };
  return (
    <PayPalScriptProvider
      options={{ clientId: "test", components: "buttons", currency: "USD" }}
    >
      <div style={{ width: "500px", margin: "auto", paddingTop: "100px" }}>
        <Cart cart={cart} setCartHandler={setCartHandler} />
        <PayPalButtons
          disabled={false}
          fundingSource={undefined}
          createOrder={createOrder}
          onApprove={onApprove}
        />
      </div>
    </PayPalScriptProvider>
  );
}
