package storageservice;

public class StorageServiceFactory {
    private static StorageServiceFactory storageServiceFactory;

    private StorageServiceFactory(){}

    public static StorageServiceFactory getStorageServiceFactory(){
        if (StorageServiceFactory.storageServiceFactory == null){
            StorageServiceFactory.storageServiceFactory = new StorageServiceFactory();
        }
        return StorageServiceFactory.storageServiceFactory;
    }

    public IStorageServiceAdapter getStorageServiceAdapter(String storageServiceAdapterName){
        IStorageServiceAdapter storageServiceAdapter = null;
        switch(storageServiceAdapterName){
            case "csv" -> storageServiceAdapter = new CSVStorageServiceAdapter();
        }
        return storageServiceAdapter;
    }
}
