#2.2.1

## VBoxManage createvm --name "Linux of Germikova 2" \
-ostype Ubuntu14_LTS_64 --register

## VBoxManage modifyvm "Linux of Germikova 2" --memory 2443

## VBoxManage modifyvm "Linux of Germikova 2" --cpus 1

## vboxmanage modifyvm "Linux of Germikova 2" --nic1 nat

## VBoxManage modifyvm "Linux of Germikova 2" \
--natpf1 "ssh,tcp,,1234,,22"

## VBoxManage storagectl "Linux of Germikova 2" --name "GermikovaSATA" --add sata --controller IntelAHCI

## VBoxManage storagectl "Linux of Germikova 2" --name "GermikovaIDE" \ 
--add ide --controller PIIX4

## VBoxManage createmedium --filename DiskGermikova.vdi --size 9760

## VBoxManage storageattach "Linux of Germikova 2" --storagectl "GermikovaIDE" \
--port 1 --device 0 --type dvddrive --medium /Users/deniza/Downloads/ubuntu-14.04.6-server-amd64.iso

# 2.2.2

## VBoxManage startvm "Linux of Germikova 2"

# 2.2.3

## VBoxManage controlvm "Linux of Germikova 2" poweroff

# 2.2.4

## VBoxManage startvm "Linux of Germikova 2"

# 2.2.5

## VBoxManage startvm "Linux of Germikova 2"

# 2.2.6

## VBoxManage controlvm "Linux of Germikova 2" poweroff
