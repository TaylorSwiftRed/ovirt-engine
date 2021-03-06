package org.ovirt.engine.core.bll.storage.disk.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.ovirt.engine.core.bll.ValidateTestUtils;
import org.ovirt.engine.core.bll.ValidationResult;
import org.ovirt.engine.core.bll.utils.PermissionSubject;
import org.ovirt.engine.core.bll.validator.storage.DiskImagesValidator;
import org.ovirt.engine.core.bll.validator.storage.DiskValidator;
import org.ovirt.engine.core.common.VdcObjectType;
import org.ovirt.engine.core.common.action.TransferDiskImageParameters;
import org.ovirt.engine.core.common.businessentities.ActionGroup;
import org.ovirt.engine.core.common.businessentities.storage.DiskImage;
import org.ovirt.engine.core.common.errors.EngineMessage;

@RunWith(MockitoJUnitRunner.class)
public class TransferDiskImageCommandTest extends TransferImageCommandTest {

    @Mock
    DiskValidator diskValidator;

    @Mock
    DiskImagesValidator diskImagesValidator;

    @Override
    protected TransferDiskImageCommand spyCommand() {
        return new TransferDiskImageCommand(new TransferDiskImageParameters(), null);
    }

    @Before
    public void setUp() {
        initCommand();
    }

    protected void initializeSuppliedImage() {
        super.initSuppliedImage(transferImageCommand);

        DiskImage diskImage = new DiskImage();
        doReturn(diskImage).when(diskDao).get(any());

        doReturn(diskValidator).when(getCommand()).getDiskValidator(any());
        doReturn(diskImagesValidator).when(getCommand()).getDiskImagesValidator(any());

        // Set validators return
        doReturn(ValidationResult.VALID)
                .when(diskValidator)
                .isDiskAttachedToAnyVm();
        doReturn(ValidationResult.VALID)
                .when(diskValidator)
                .isDiskExists();
        doReturn(ValidationResult.VALID)
                .when(diskImagesValidator)
                .diskImagesNotLocked();
        doReturn(ValidationResult.VALID)
                .when(diskImagesValidator)
                .diskImagesNotIllegal();
    }

    @Test
    public void validate() {
        initializeSuppliedImage();
        assertTrue(getCommand().validate());
    }

    @Test
    public void validateCantUploadLockedImage() {
        initializeSuppliedImage();
        doReturn(new ValidationResult(EngineMessage.ACTION_TYPE_FAILED_DISKS_LOCKED, ""))
                .when(diskImagesValidator)
                .diskImagesNotLocked();

        getCommand().validate();
        ValidateTestUtils.assertValidationMessages(
                "Can't start a transfer for a locked image.",
                getCommand(),
                EngineMessage.ACTION_TYPE_FAILED_DISKS_LOCKED);
    }

    @Test
    public void validateCantUploadDiskAttached() {
        initializeSuppliedImage();
        doReturn(new ValidationResult(EngineMessage.ACTION_TYPE_FAILED_DISK_ATTACHED_TO_VMS, ""))
                .when(diskValidator)
                .isDiskAttachedToAnyVm();

        getCommand().validate();
        ValidateTestUtils.assertValidationMessages(
                "Can't start a transfer for an image that is attached to any VMs.",
                getCommand(),
                EngineMessage.ACTION_TYPE_FAILED_DISK_ATTACHED_TO_VMS);
    }

    @Test
    public void validateCantUploadDiskNotExists() {
        initializeSuppliedImage();
        doReturn(new ValidationResult(EngineMessage.ACTION_TYPE_FAILED_DISK_NOT_EXIST, ""))
                .when(diskValidator)
                .isDiskExists();

        getCommand().validate();
        ValidateTestUtils.assertValidationMessages(
                "Can't start a transfer for image that doesn't exist.",
                getCommand(),
                EngineMessage.ACTION_TYPE_FAILED_DISK_NOT_EXIST);
    }

    @Test
    public void validateCantUploadIllegalImage() {
        initializeSuppliedImage();
        doReturn(new ValidationResult(EngineMessage.ACTION_TYPE_FAILED_DISKS_ILLEGAL, ""))
                .when(diskImagesValidator)
                .diskImagesNotIllegal();

        getCommand().validate();
        ValidateTestUtils.assertValidationMessages(
                "Can't start a transfer for an illegal image.",
                getCommand(),
                EngineMessage.ACTION_TYPE_FAILED_DISKS_ILLEGAL);
    }

    @Test
    public void testPermissionSubjectOnProvidedImage() {
        initializeSuppliedImage();
        assertEquals(getCommand().getPermissionCheckSubjects().get(0),
                new PermissionSubject(getCommand().getParameters().getImageId(),
                        VdcObjectType.Disk,
                        ActionGroup.EDIT_DISK_PROPERTIES));
    }

    @Test
    public void testPermissionSubjectOnNewImage() {
        assertEquals(getCommand().getPermissionCheckSubjects().get(0),
                new PermissionSubject(getCommand().getParameters().getImageId(),
                        VdcObjectType.Storage,
                        ActionGroup.CREATE_DISK));
    }

    private TransferDiskImageCommand<TransferDiskImageParameters> getCommand() {
        return (TransferDiskImageCommand) transferImageCommand;
    }
}
