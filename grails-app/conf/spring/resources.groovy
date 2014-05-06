import ru.jconsulting.igetit.marshallers.*

// Place your Spring DSL code here
beans = {
    customMarshallerRegistrar(MarshallerListRegistrar) {
        marshallerList = [
                new BrandMarshaller(),
                new BuyMarshaller(),
                new CategoryMarshaller(),
                new CommentMarshaller(),
                new ImageMarshaller(),
                new UserMarshaller(),
                new PriceMarshaller()
        ]
    }
}
