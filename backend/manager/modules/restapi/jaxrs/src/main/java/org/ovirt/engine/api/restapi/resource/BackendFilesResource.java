package org.ovirt.engine.api.restapi.resource;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.ovirt.engine.api.model.File;
import org.ovirt.engine.api.model.Files;
import org.ovirt.engine.api.model.StorageDomain;
import org.ovirt.engine.api.resource.FileResource;
import org.ovirt.engine.api.resource.FilesResource;
import org.ovirt.engine.core.common.businessentities.RepoFileMetaData;
import org.ovirt.engine.core.common.businessentities.ImageType;

import org.ovirt.engine.core.common.queries.GetImagesListParameters;
import org.ovirt.engine.core.common.queries.StorageDomainQueryParametersBase;
import org.ovirt.engine.core.common.queries.VdcQueryType;

import static org.ovirt.engine.api.restapi.resource.BackendStorageDomainResource.isIsoDomain;

public class BackendFilesResource
    extends AbstractBackendCollectionResource<File, String>
    implements FilesResource {

    protected String storageDomainId;

    public BackendFilesResource(String storageDomainId) {
        super(File.class, String.class);
        this.storageDomainId = storageDomainId;
    }

    @Override
    public Files list() {
        if (isIsoDomain(getEntity(org.ovirt.engine.core.common.businessentities.StorageDomain.class,
                                  VdcQueryType.GetStorageDomainById,
                                  new StorageDomainQueryParametersBase(asGuid(storageDomainId)),
                                  "storage_domain"))) {
            return mapCollection(listFiles());
        } else {
            return notFound(Files.class);
        }
    }

    @Override
    @SingleEntityResource
    public FileResource getFileSubResource(String id) {
        return new BackendFileResource(id, this);
    }

    @Override
    protected File addParents(File file) {
        file.setStorageDomain(new StorageDomain());
        file.getStorageDomain().setId(storageDomainId);
        return file;
    }

    protected Files mapCollection(List<String> entities) {
        Files files = new Files();
        for (String name : entities) {
            files.getFiles().add(addLinks(map(name)));
        }
        return files;
    }

    public File lookupFile(String name) {
        if (listFiles().contains(name)) {
            return addLinks(map(name));
        } else {
            return notFound();
        }
    }

    protected List<String> listFiles() {
        GetImagesListParameters queryParams = new GetImagesListParameters(asGuid(storageDomainId), ImageType.ISO);
        queryParams.setForceRefresh(true);

        List<RepoFileMetaData> files = getBackendCollection(RepoFileMetaData.class, VdcQueryType.GetImagesList,
                                    queryParams);
        List<String> fileNames = new LinkedList<String>();
        for (RepoFileMetaData file : files) {
            fileNames.add(file.getRepoFileName());
        }
        return fileNames;
    }

    @Override
    protected Response performRemove(String id) {
       throw new UnsupportedOperationException();
    }

    @Override
    protected File doPopulate(File model, String entity) {
        return model;
    }

}
