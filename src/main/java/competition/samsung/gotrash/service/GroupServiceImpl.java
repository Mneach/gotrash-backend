package competition.samsung.gotrash.service;

import competition.samsung.gotrash.constant.ServiceName;
import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.repository.GroupRepository;
import competition.samsung.gotrash.utils.S3BucketUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService{

    private GroupRepository groupRepository;
    private S3Service s3Service;

    @Override
    public List<Group> findAll() throws Exception {
        List<Group> groups = groupRepository.findAll();

        for(Group group : groups){
            if(!Objects.equals(group.getTargetReward().getImageUrl(), "")){
                Reward reward = group.getTargetReward();
                String objectKeys = S3BucketUtil.createObjectKey(ServiceName.REWARD, reward.getId(), reward.getImageName());
                String presignUrl = s3Service.getPresignUrl(objectKeys);
                reward.setImageUrl(presignUrl);
                group.setTargetReward(reward);
            }
        }

        return groups;
    }

    @Override
    public Optional<Group> findById(String id) throws Exception {
        Optional<Group> optionalGroup = groupRepository.findById(id);

        if(optionalGroup.isEmpty()){
            return optionalGroup;
        }

        Group group = optionalGroup.get();
        if(!Objects.equals(group.getTargetReward().getImageUrl(), "")){
            Reward reward = group.getTargetReward();
            String objectKeys = S3BucketUtil.createObjectKey(ServiceName.REWARD, reward.getId(), reward.getImageName());
            String presignUrl = s3Service.getPresignUrl(objectKeys);
            reward.setImageUrl(presignUrl);
            group.setTargetReward(reward);
        }

        return Optional.of(group);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public String delete(String id) {
        groupRepository.deleteById(id);
        return "Successfully delete group with id " + id;
    }
}
