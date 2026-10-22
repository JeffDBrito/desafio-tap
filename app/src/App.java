import Data.Base;

public class App {
    public static void main(String[] args) throws Exception {

        Base base = Base.getInstance();
        base.criarProjetosBase();

        System.out.println(base.getProjetos());

    }
}
